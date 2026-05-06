package com.vashuag.grocery.feature.compare.data

import com.vashuag.grocery.data.entity.ComparisonHistory
import com.vashuag.grocery.data.entity.ComparisonHistory_
import com.vashuag.grocery.feature.compare.data.adapters.PriceSourceAdapter
import com.vashuag.grocery.feature.compare.domain.CompareRequest
import com.vashuag.grocery.feature.compare.domain.CompareResult
import com.vashuag.grocery.feature.compare.domain.Offer
import com.vashuag.grocery.feature.compare.domain.OfferMatcher
import com.vashuag.grocery.feature.compare.domain.UserLocation
import com.vashuag.grocery.feature.compare.domain.normalizeText
import io.objectbox.Box
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceComparisonRepository @Inject constructor(
    private val adapters: List<@JvmSuppressWildcards PriceSourceAdapter>,
    private val matcher: OfferMatcher,
    private val historyBox: Box<ComparisonHistory>
) {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    suspend fun compare(request: CompareRequest): CompareResult {
        val now = System.currentTimeMillis()
        if (!request.isValid()) {
            return CompareResult(
                query = request.queryText,
                location = request.location,
                matchedItems = emptyList(),
                generatedAtMs = now,
                fromCache = false,
                rawSiteResults = emptyMap(),
                siteErrors = mapOf("request" to "Invalid request")
            )
        }

        val fingerprint = fingerprint(request.queryText, request.location)
        val cached = getRecentCache(fingerprint)
        if (cached != null) {
            val cachedResult = runCatching {
                json.decodeFromString<CompareResult>(cached.responseJson)
            }.getOrNull()
            if (cachedResult != null) {
                return cachedResult.copy(fromCache = true)
            }
        }

        val siteResults = fetchAllSites(request)
        val rawSiteResults = siteResults.associate { it.site to it.offers }
        val siteErrors = siteResults.associate { it.site to it.error }
        val flatOffers = siteResults.flatMap { it.offers }

        val matched = matcher.match(request.queryText, flatOffers)

        val response = CompareResult(
            query = request.queryText,
            location = request.location,
            matchedItems = matched,
            generatedAtMs = now,
            fromCache = false,
            rawSiteResults = rawSiteResults,
            siteErrors = siteErrors
        )

        storeCache(
            fingerprint = fingerprint,
            queryText = request.queryText,
            location = request.location,
            result = response
        )

        return response
    }

    private suspend fun fetchAllSites(request: CompareRequest): List<SiteResult> = coroutineScope {
        adapters.map { adapter ->
            async(Dispatchers.IO) {
                runCatching {
                    val offers = adapter.search(
                        query = request.queryText,
                        location = request.location,
                        maxResults = request.maxResultsPerSite
                    )
                    val filtered = filterRelevantOffers(request.queryText, offers)
                    if (filtered.isEmpty()) {
                        SiteResult(
                            site = adapter.name,
                            offers = emptyList(),
                            error = "No parsable offers from site or blocked by anti-bot/location gate"
                        )
                    } else {
                        SiteResult(site = adapter.name, offers = filtered.take(request.maxResultsPerSite), error = null)
                    }
                }.getOrElse { throwable ->
                    SiteResult(
                        site = adapter.name,
                        offers = emptyList(),
                        error = throwable.message?.ifBlank { null }
                            ?: "${throwable::class.simpleName} while fetching site data"
                    )
                }
            }
        }.awaitAll()
    }

    private fun filterRelevantOffers(query: String, offers: List<Offer>): List<Offer> {
        val queryNorm = normalizeText(query)
        if (queryNorm.isBlank()) {
            return offers
        }

        val ranked = offers.mapNotNull { offer ->
            var score = matcher.offerQueryScore(query, offer)
            val titleNorm = normalizeText(offer.title)
            if (queryNorm in titleNorm) {
                score += 12.0
            }
            if (score >= 44.0) {
                score to offer
            } else {
                null
            }
        }.sortedByDescending { it.first }

        return if (ranked.isNotEmpty()) {
            ranked.map { it.second }
        } else {
            offers
        }
    }

    private fun getRecentCache(fingerprint: String): ComparisonHistory? {
        val ttlMs = 30L * 60L * 1000L
        val cutoff = System.currentTimeMillis() - ttlMs

        val query = historyBox.query(ComparisonHistory_.fingerprint.equal(fingerprint)).build()
        return try {
            query.find()
                .asSequence()
                .filter { history -> history.createdAtMs >= cutoff }
                .maxByOrNull { history -> history.createdAtMs }
        } finally {
            query.close()
        }
    }

    private fun storeCache(
        fingerprint: String,
        queryText: String,
        location: UserLocation,
        result: CompareResult
    ) {
        val locationJson = json.encodeToString(location)
        val responseJson = json.encodeToString(result)
        historyBox.put(
            ComparisonHistory(
                fingerprint = fingerprint,
                queryText = queryText,
                locationJson = locationJson,
                responseJson = responseJson,
                createdAtMs = System.currentTimeMillis()
            )
        )
    }

    private fun fingerprint(query: String, location: UserLocation): String {
        val locationMap = listOfNotNull(
            location.city?.let { "city=${it.lowercase()}" },
            location.area?.let { "area=${it.lowercase()}" },
            location.pincode?.filter { it.isDigit() }?.let { "pincode=$it" },
            location.latitude?.let { "lat=$it" },
            location.longitude?.let { "lng=$it" }
        ).sorted().joinToString("|")

        val payload = "${normalizeText(query)}|$locationMap"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(payload.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }

    private data class SiteResult(
        val site: String,
        val offers: List<Offer>,
        val error: String?
    )
}
