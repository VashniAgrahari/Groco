package com.vashuag.grocery.feature.compare.domain

private val stopWords = setOf(
    "pack",
    "combo",
    "online",
    "buy",
    "delivery",
    "fresh",
    "minutes",
    "minute",
    "offer",
    "best",
    "sale"
)

private val unitMultipliers = mapOf(
    "kg" to 1000.0,
    "g" to 1.0,
    "l" to 1000.0,
    "ml" to 1.0
)

data class Quantity(
    val value: Double,
    val unit: String
) {
    fun normalized(): Pair<Double, String> {
        val normalizedUnit = unit.lowercase()
        val multiplier = unitMultipliers[normalizedUnit]
        if (multiplier == null) {
            return value to normalizedUnit
        }

        val baseUnit = if (normalizedUnit == "kg" || normalizedUnit == "g") "g" else "ml"
        return (value * multiplier) to baseUnit
    }
}

fun normalizeText(text: String): String {
    val lowered = text.lowercase()
    val cleaned = lowered.replace("[^a-z0-9\\s]".toRegex(), " ")
        .replace("\\s+".toRegex(), " ")
        .trim()

    if (cleaned.isBlank()) {
        return ""
    }

    return cleaned.split(" ")
        .filter { token -> token.isNotBlank() && token !in stopWords }
        .joinToString(" ")
}

fun extractQuantity(text: String?): Quantity? {
    if (text.isNullOrBlank()) {
        return null
    }

    val regex = "(\\d+(?:\\.\\d+)?)\\s*(kg|g|l|ltr|litre|liter|ml|pcs|pc|pack)".toRegex(RegexOption.IGNORE_CASE)
    val matches = regex.findAll(text).toList()
    if (matches.isEmpty()) {
        return null
    }

    val preferred = matches.firstOrNull { match ->
        val unit = match.groupValues[2].lowercase()
        unit in setOf("kg", "g", "l", "ltr", "litre", "liter", "ml")
    } ?: matches.first()

    val value = preferred.groupValues[1].toDoubleOrNull() ?: return null
    var unit = preferred.groupValues[2].lowercase()

    if (unit == "pc" || unit == "pcs") {
        unit = "pcs"
    }
    if (unit == "ltr" || unit == "litre" || unit == "liter") {
        unit = "l"
    }

    return Quantity(value = value, unit = unit)
}

fun quantityCompatibility(left: String?, right: String?): Double {
    val q1 = extractQuantity(left)
    val q2 = extractQuantity(right)

    if (q1 == null || q2 == null) {
        return 0.75
    }

    val (value1, unit1) = q1.normalized()
    val (value2, unit2) = q2.normalized()

    if (unit1 != unit2) {
        return 0.25
    }

    val maxValue = maxOf(value1, value2)
    if (maxValue == 0.0) {
        return 0.5
    }

    val distance = kotlin.math.abs(value1 - value2) / maxValue
    return when {
        distance <= 0.1 -> 1.0
        distance <= 0.25 -> 0.8
        distance <= 0.5 -> 0.6
        else -> 0.3
    }
}
