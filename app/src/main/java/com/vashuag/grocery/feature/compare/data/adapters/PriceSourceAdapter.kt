package com.vashuag.grocery.feature.compare.data.adapters

import com.vashuag.grocery.feature.compare.domain.Offer
import com.vashuag.grocery.feature.compare.domain.UserLocation

interface PriceSourceAdapter {
    val name: String

    suspend fun search(
        query: String,
        location: UserLocation,
        maxResults: Int
    ): List<Offer>
}
