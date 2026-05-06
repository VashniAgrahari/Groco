package com.vashuag.grocery.feature.compare.domain

import kotlinx.serialization.Serializable

@Serializable
data class UserLocation(
    val city: String? = null,
    val area: String? = null,
    val pincode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)

@Serializable
data class CompareRequest(
    val location: UserLocation = UserLocation(),
    val itemName: String? = null,
    val searchTag: String? = null,
    val maxResultsPerSite: Int = 8
) {
    val queryText: String
        get() = (itemName ?: searchTag ?: "").trim()

    fun isValid(): Boolean {
        return queryText.isNotBlank() && maxResultsPerSite in 1..30
    }
}

@Serializable
data class Offer(
    val site: String,
    val title: String,
    val price: Double,
    val currency: String = "INR",
    val brand: String? = null,
    val sizeText: String? = null,
    val inStock: Boolean? = null,
    val url: String? = null,
    val imageUrl: String? = null,
    val score: Double? = null,
    val raw: Map<String, String> = emptyMap()
)

@Serializable
data class MatchedItem(
    val canonicalName: String,
    val confidence: Double,
    val offers: List<Offer>
)

@Serializable
data class CompareResult(
    val query: String,
    val location: UserLocation,
    val matchedItems: List<MatchedItem>,
    val generatedAtMs: Long,
    val fromCache: Boolean = false,
    val rawSiteResults: Map<String, List<Offer>> = emptyMap(),
    val siteErrors: Map<String, String?> = emptyMap()
)
