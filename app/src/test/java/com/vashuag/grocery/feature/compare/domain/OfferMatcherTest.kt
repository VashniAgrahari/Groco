package com.vashuag.grocery.feature.compare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OfferMatcherTest {

    @Test
    fun matcher_clustersSimilarOffers() {
        val matcher = OfferMatcher(clusterThreshold = 65.0)
        val offers = listOf(
            Offer(site = "blinkit", title = "Amul Gold Milk 500 ml", price = 35.0, sizeText = "500 ml"),
            Offer(site = "zepto", title = "Amul Gold Fresh Milk 500ml", price = 34.0, sizeText = "500 ml"),
            Offer(site = "jiomart", title = "Amul Taaza Milk 1 L", price = 68.0, sizeText = "1 l")
        )

        val matched = matcher.match(queryText = "amul gold milk", offers = offers)

        assertTrue(matched.isNotEmpty())
        assertTrue(matched.first().canonicalName.lowercase().startsWith("amul"))
        assertEquals(2, matched.first().offers.size)
        assertEquals(34.0, matched.first().offers.first().price, 0.001)
    }

    @Test
    fun matcher_handlesEmptyOffers() {
        val matcher = OfferMatcher()
        val matched = matcher.match(queryText = "banana", offers = emptyList())
        assertTrue(matched.isEmpty())
    }
}
