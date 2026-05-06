package com.vashuag.grocery.feature.compare.data

import com.vashuag.grocery.feature.compare.data.adapters.AmazonNowAdapter
import com.vashuag.grocery.feature.compare.data.adapters.BigBasketAdapter
import com.vashuag.grocery.feature.compare.data.adapters.BlinkitAdapter
import com.vashuag.grocery.feature.compare.data.adapters.FlipkartMinutesAdapter
import com.vashuag.grocery.feature.compare.data.adapters.JioMartAdapter
import com.vashuag.grocery.feature.compare.data.adapters.PriceSourceAdapter
import com.vashuag.grocery.feature.compare.data.adapters.SwiggyInstamartAdapter
import com.vashuag.grocery.feature.compare.data.adapters.ZeptoAdapter
import com.vashuag.grocery.feature.compare.domain.OfferMatcher
import com.vashuag.grocery.feature.compare.domain.UserLocation
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assume.assumeTrue
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PriceComparatorCatalogLiveTest {

    @Test
    fun liveCatalogComparisonAcrossCategories() = runBlocking {
        val runLive = System.getenv("RUN_LIVE_COMPARE_TESTS")
            ?.equals("true", ignoreCase = true) == true
        assumeTrue(
            "Set RUN_LIVE_COMPARE_TESTS=true to run live comparator catalog test",
            runLive
        )

        val client = HttpClient(OkHttp) {
            install(HttpTimeout) {
                requestTimeoutMillis = 10_000
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 12_000
            }
        }

        try {
            val adapters: List<PriceSourceAdapter> = listOf(
                BlinkitAdapter(client),
                SwiggyInstamartAdapter(client),
                ZeptoAdapter(client),
                AmazonNowAdapter(client),
                JioMartAdapter(client),
                BigBasketAdapter(client),
                FlipkartMinutesAdapter(client)
            )

            val matcher = OfferMatcher()
            val location = UserLocation(
                city = "Bengaluru",
                area = "Indiranagar",
                pincode = "560038",
                latitude = 12.9716,
                longitude = 77.5946
            )

            val catalog = listOf(
                CatalogItem("Dairy", "amul milk"),
                CatalogItem("Dairy", "mother dairy curd"),
                CatalogItem("Grains", "basmati rice"),
                CatalogItem("Grains", "atta"),
                CatalogItem("Pulses", "toor dal"),
                CatalogItem("Cooking", "sunflower oil"),
                CatalogItem("Snacks", "lays chips"),
                CatalogItem("Snacks", "oreo biscuits"),
                CatalogItem("Beverages", "coca cola"),
                CatalogItem("Beverages", "tata tea"),
                CatalogItem("Personal Care", "dove soap"),
                CatalogItem("Home Care", "surf excel detergent"),
                CatalogItem("Produce", "banana"),
                CatalogItem("Produce", "tomato")
            )

            val reportLines = mutableListOf<String>()
            val itemSummaries = mutableListOf<ItemSummary>()
            val adapterErrors = mutableListOf<String>()

            reportLines += "# Price Comparator Live Catalog Report"
            reportLines += ""
            reportLines += "Generated: ${timestamp()}"
            reportLines += "Location: ${location.city}, ${location.pincode}"
            reportLines += "Adapters: ${adapters.joinToString { it.name }}"
            reportLines += ""

            for (item in catalog) {
                val siteCounts = linkedMapOf<String, Int>()
                val flatOffers = mutableListOf<com.vashuag.grocery.feature.compare.domain.Offer>()

                for (adapter in adapters) {
                    val offers = runCatching {
                        withTimeoutOrNull(20_000) {
                            adapter.search(
                                query = item.query,
                                location = location,
                                maxResults = 6
                            )
                        } ?: emptyList()
                    }.getOrElse { throwable ->
                        adapterErrors += "${item.query} @ ${adapter.name}: ${throwable.message}"
                        emptyList()
                    }

                    siteCounts[adapter.name] = offers.size
                    flatOffers += offers
                }

                val scoredOffers = flatOffers.map { offer ->
                    offer to matcher.offerQueryScore(item.query, offer)
                }
                val relevantOffers = scoredOffers
                    .filter { (_, score) -> score >= 44.0 }
                    .map { (offer, _) -> offer }
                val topScore = scoredOffers.maxOfOrNull { (_, score) -> score } ?: 0.0
                val matchedBuckets = matcher.match(item.query, flatOffers)
                val bestPrice = flatOffers.minOfOrNull { it.price }
                val bestSite = flatOffers.minByOrNull { it.price }?.site

                itemSummaries += ItemSummary(
                    category = item.category,
                    query = item.query,
                    totalOffers = flatOffers.size,
                    relevantOffers = relevantOffers.size,
                    matchedBuckets = matchedBuckets.size,
                    topOfferScore = topScore,
                    bestPrice = bestPrice,
                    bestSite = bestSite,
                    perSite = siteCounts
                )

                reportLines += "## ${item.category}: ${item.query}"
                reportLines += "- Offers found: ${flatOffers.size}"
                reportLines += "- Relevant offers (score >= 44): ${relevantOffers.size}"
                reportLines += "- Matched buckets: ${matchedBuckets.size}"
                reportLines += "- Top offer score: ${"%.2f".format(topScore)}"
                reportLines += "- Best price: ${bestPrice?.let { "₹${"%.2f".format(it)} @ $bestSite" } ?: "N/A"}"
                reportLines += "- Per site: ${siteCounts.entries.joinToString { "${it.key}=${it.value}" }}"
                reportLines += ""
            }

            val itemsWithRelevantOffers = itemSummaries.count { it.relevantOffers > 0 }
            val totalItems = itemSummaries.size
            val successRatio = if (totalItems == 0) 0.0 else itemsWithRelevantOffers.toDouble() / totalItems.toDouble()
            val totalOffers = itemSummaries.sumOf { it.totalOffers }
            val totalRelevantOffers = itemSummaries.sumOf { it.relevantOffers }

            reportLines += "## Summary"
            reportLines += "- Catalog items tested: $totalItems"
            reportLines += "- Items with at least one relevant offer: $itemsWithRelevantOffers"
            reportLines += "- Success ratio: ${"%.2f".format(successRatio * 100)}%"
            reportLines += "- Total offers collected: $totalOffers"
            reportLines += "- Total relevant offers: $totalRelevantOffers"
            reportLines += "- Adapter exceptions: ${adapterErrors.size}"

            if (adapterErrors.isNotEmpty()) {
                reportLines += ""
                reportLines += "## Adapter Exceptions"
                adapterErrors.forEach { error ->
                    reportLines += "- $error"
                }
            }

            val reportDir = File("build/reports/price-comparator")
            reportDir.mkdirs()
            val reportFile = File(reportDir, "catalog-live-report.md")
            reportFile.writeText(reportLines.joinToString("\n"))

            // Stability criteria for live-network smoke run.
            assertTrue(
                "Expected no adapter-level exceptions. See report: ${reportFile.absolutePath}",
                adapterErrors.isEmpty()
            )
            assertTrue(
                "Expected at least 50% items to return relevant offers, got ${(successRatio * 100).toInt()}%. See report: ${reportFile.absolutePath}",
                successRatio >= 0.50
            )
        } finally {
            client.close()
        }
    }

    private fun timestamp(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault())
        return formatter.format(Date())
    }

    private data class CatalogItem(
        val category: String,
        val query: String
    )

    private data class ItemSummary(
        val category: String,
        val query: String,
        val totalOffers: Int,
        val relevantOffers: Int,
        val matchedBuckets: Int,
        val topOfferScore: Double,
        val bestPrice: Double?,
        val bestSite: String?,
        val perSite: Map<String, Int>
    )
}
