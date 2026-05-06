package com.vashuag.grocery.feature.compare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CompareTextUtilsTest {

    @Test
    fun extractQuantity_parsesSimpleMetricValue() {
        val quantity = extractQuantity("Amul Milk 500 ml")
        assertNotNull(quantity)
        assertEquals(500.0, quantity!!.value, 0.001)
        assertEquals("ml", quantity.unit)
    }

    @Test
    fun extractQuantity_prefersComparableUnitOverPackaging() {
        val quantity = extractQuantity("Amul Ice Cream 1 pack (1 L)")
        assertNotNull(quantity)
        assertEquals(1.0, quantity!!.value, 0.001)
        assertEquals("l", quantity.unit)
    }

    @Test
    fun quantityCompatibility_sameScaleIsHigh() {
        val score = quantityCompatibility("500 ml", "0.5 l")
        assertTrue(score >= 0.9)
    }
}
