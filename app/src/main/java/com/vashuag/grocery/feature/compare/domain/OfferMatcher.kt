package com.vashuag.grocery.feature.compare.domain

class OfferMatcher(
    private val clusterThreshold: Double = 65.0
) {

    fun match(queryText: String, offers: List<Offer>): List<MatchedItem> {
        if (offers.isEmpty()) {
            return emptyList()
        }

        val quantityGroups = groupByQuantity(offers)
        val clusters = mutableListOf<List<Offer>>()
        quantityGroups.values.forEach { group ->
            clusters += clusterOffers(group)
        }

        val ranked = clusters.sortedByDescending { bucket ->
            clusterQueryScore(queryText, bucket)
        }

        return ranked.map { bucket ->
            val confidence = (clusterQueryScore(queryText, bucket) / 100.0)
                .coerceIn(0.0, 1.0)
            val sortedOffers = bucket
                .map { offer ->
                    offer.copy(score = round2(offerQueryScore(queryText, offer)))
                }
                .sortedBy { it.price }

            MatchedItem(
                canonicalName = canonicalName(bucket),
                confidence = round3(confidence),
                offers = sortedOffers
            )
        }
    }

    fun offerQueryScore(queryText: String, offer: Offer): Double {
        val query = normalizeText(queryText)
        val title = normalizeText(offer.title)
        if (query.isBlank() || title.isBlank()) {
            return 0.0
        }
        return (0.7 * tokenSetRatio(query, title)) + (0.3 * partialRatio(query, title))
    }

    private fun groupByQuantity(offers: List<Offer>): Map<String, List<Offer>> {
        return offers.groupBy { offer -> quantityBucket(offer) }
    }

    private fun clusterOffers(offers: List<Offer>): List<List<Offer>> {
        val clusters = mutableListOf<MutableList<Offer>>()

        for (offer in offers) {
            var placed = false
            for (cluster in clusters) {
                val anchor = cluster.first()
                if (offerSimilarity(anchor, offer) >= clusterThreshold) {
                    cluster.add(offer)
                    placed = true
                    break
                }
            }
            if (!placed) {
                clusters.add(mutableListOf(offer))
            }
        }

        return clusters
    }

    private fun offerSimilarity(left: Offer, right: Offer): Double {
        val leftText = normalizeText(left.title)
        val rightText = normalizeText(right.title)
        val textScore = tokenSetRatio(leftText, rightText)

        val qtyCompat = quantityCompatibility(left.sizeText ?: left.title, right.sizeText ?: right.title)
        val quantityScore = qtyCompat * 100.0

        val brandScore = if (!left.brand.isNullOrBlank() && !right.brand.isNullOrBlank()) {
            ratio(normalizeText(left.brand), normalizeText(right.brand))
        } else {
            70.0
        }

        var score = (0.55 * textScore) + (0.3 * quantityScore) + (0.15 * brandScore)
        if (qtyCompat < 0.55) {
            score -= 20.0
        }
        return score.coerceAtLeast(0.0)
    }

    private fun clusterQueryScore(queryText: String, cluster: List<Offer>): Double {
        if (cluster.isEmpty()) {
            return 0.0
        }
        return cluster.map { offer -> offerQueryScore(queryText, offer) }.average()
    }

    private fun canonicalName(offers: List<Offer>): String {
        if (offers.isEmpty()) {
            return ""
        }

        val frequency = mutableMapOf<String, Int>()
        offers.forEach { offer ->
            val key = normalizeText(offer.title)
            frequency[key] = (frequency[key] ?: 0) + 1
        }

        val mostCommonKey = frequency.maxByOrNull { entry -> entry.value }?.key
        if (mostCommonKey == null) {
            return offers.first().title
        }

        return offers.firstOrNull { normalizeText(it.title) == mostCommonKey }?.title
            ?: offers.first().title
    }

    private fun quantityBucket(offer: Offer): String {
        val quantity = extractQuantity(offer.sizeText ?: offer.title) ?: return "na"
        val (value, unit) = quantity.normalized()
        return "$unit:${kotlin.math.round(value).toInt()}"
    }

    private fun ratio(left: String, right: String): Double {
        if (left == right) {
            return 100.0
        }
        if (left.isEmpty() || right.isEmpty()) {
            return 0.0
        }
        val distance = levenshteinDistance(left, right)
        val maxLen = maxOf(left.length, right.length)
        return ((1.0 - (distance.toDouble() / maxLen.toDouble())) * 100.0).coerceIn(0.0, 100.0)
    }

    private fun tokenSetRatio(left: String, right: String): Double {
        val leftTokens = left.split(" ").filter { it.isNotBlank() }.toSet()
        val rightTokens = right.split(" ").filter { it.isNotBlank() }.toSet()
        if (leftTokens.isEmpty() || rightTokens.isEmpty()) {
            return 0.0
        }

        val intersect = leftTokens.intersect(rightTokens)
        if (intersect.isEmpty()) {
            return ratio(left, right)
        }

        val leftComp = (intersect + (leftTokens - rightTokens)).sorted().joinToString(" ")
        val rightComp = (intersect + (rightTokens - leftTokens)).sorted().joinToString(" ")
        val base = intersect.sorted().joinToString(" ")

        return maxOf(
            ratio(base, leftComp),
            ratio(base, rightComp),
            ratio(leftComp, rightComp)
        )
    }

    private fun partialRatio(left: String, right: String): Double {
        val shorter: String
        val longer: String
        if (left.length <= right.length) {
            shorter = left
            longer = right
        } else {
            shorter = right
            longer = left
        }

        if (shorter.isEmpty()) {
            return 0.0
        }

        var best = 0.0
        val span = shorter.length
        var index = 0
        while (index + span <= longer.length) {
            val candidate = longer.substring(index, index + span)
            best = maxOf(best, ratio(shorter, candidate))
            if (best >= 99.9) {
                break
            }
            index += 1
        }

        return best
    }

    private fun levenshteinDistance(left: String, right: String): Int {
        val rows = left.length + 1
        val cols = right.length + 1
        val dp = Array(rows) { IntArray(cols) }

        for (i in 0 until rows) dp[i][0] = i
        for (j in 0 until cols) dp[0][j] = j

        for (i in 1 until rows) {
            for (j in 1 until cols) {
                val cost = if (left[i - 1] == right[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }

        return dp[left.length][right.length]
    }

    private fun round2(value: Double): Double {
        return kotlin.math.round(value * 100.0) / 100.0
    }

    private fun round3(value: Double): Double {
        return kotlin.math.round(value * 1000.0) / 1000.0
    }
}
