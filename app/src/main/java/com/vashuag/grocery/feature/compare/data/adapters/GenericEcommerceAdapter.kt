package com.vashuag.grocery.feature.compare.data.adapters

import com.vashuag.grocery.feature.compare.domain.Offer
import com.vashuag.grocery.feature.compare.domain.UserLocation
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.round

abstract class GenericEcommerceAdapter(
    protected val httpClient: HttpClient
) : PriceSourceAdapter {

    abstract val searchUrlTemplate: String

    protected open val userAgent: String = (
        "Mozilla/5.0 (Linux; Android 14; Pixel 7) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36"
        )

    override suspend fun search(query: String, location: UserLocation, maxResults: Int): List<Offer> {
        if (searchUrlTemplate.isBlank() || query.isBlank()) {
            return emptyList()
        }

        val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        val url = searchUrlTemplate.replace("{query}", encoded)

        val response = runCatching {
            httpClient.get(url) {
                header("user-agent", userAgent)
                header("accept", "text/html,application/xhtml+xml,application/json")
                header("accept-language", "en-IN,en;q=0.9")
                if (!location.pincode.isNullOrBlank()) {
                    header("x-location-pincode", location.pincode)
                }
            }
        }.getOrNull() ?: return emptyList()

        if (response.status.value !in 200..299) {
            return emptyList()
        }

        val html = runCatching { response.bodyAsText() }.getOrNull() ?: return emptyList()
        val parsed = parseOffers(html = html, baseUrl = url, maxResults = maxResults)
        return parsed
            .map { offer -> offer.copy(site = name) }
            .let { dedupeOffers(it, maxResults) }
    }

    protected open fun parseOffers(
        html: String,
        baseUrl: String,
        maxResults: Int
    ): List<Offer> {
        val soup = Jsoup.parse(html)
        val collected = mutableListOf<Offer>()

        parseLdJsonProducts(soup).forEach { offer ->
            collected.add(offer)
            if (collected.size >= maxResults) {
                return dedupeOffers(collected, maxResults)
            }
        }

        parseEmbeddedJsonProducts(soup).forEach { offer ->
            collected.add(offer)
            if (collected.size >= maxResults) {
                return dedupeOffers(collected, maxResults)
            }
        }

        parseProductCards(soup, baseUrl).forEach { offer ->
            collected.add(offer)
            if (collected.size >= maxResults) {
                return dedupeOffers(collected, maxResults)
            }
        }

        return dedupeOffers(collected, maxResults)
    }

    private fun parseLdJsonProducts(soup: org.jsoup.nodes.Document): List<Offer> {
        val offers = mutableListOf<Offer>()
        val json = Json { ignoreUnknownKeys = true }

        soup.select("script[type=application/ld+json]").forEach { script ->
            val payload = script.data().ifBlank { script.html() }.trim()
            if (payload.isBlank()) {
                return@forEach
            }

            val element = runCatching { json.parseToJsonElement(payload) }.getOrNull() ?: return@forEach
            walkProductNodes(element).forEach { node ->
                mapProductNode(node)?.let { offers.add(it) }
            }
        }

        return offers
    }

    private fun parseEmbeddedJsonProducts(soup: org.jsoup.nodes.Document): List<Offer> {
        val offers = mutableListOf<Offer>()
        val json = Json { ignoreUnknownKeys = true }

        soup.select("script").forEach { script ->
            val content = script.data().ifBlank { script.html() }
            if (content.length < 20) {
                return@forEach
            }

            val candidate = content.trim()
            if (!candidate.startsWith("{") && !candidate.startsWith("[")) {
                return@forEach
            }

            val element = runCatching { json.parseToJsonElement(candidate) }.getOrNull() ?: return@forEach
            walkProductNodes(element).forEach { node ->
                mapProductNode(node)?.let { offers.add(it) }
            }
        }

        return offers
    }

    private fun parseProductCards(
        soup: org.jsoup.nodes.Document,
        baseUrl: String
    ): List<Offer> {
        val offers = mutableListOf<Offer>()
        val selectors = listOf(
            "[data-testid*=product]",
            "article",
            ".product",
            ".ProductCard",
            "li[class*=product]",
            "div[class*=product]"
        )

        selectors.forEach { selector ->
            soup.select(selector).forEach { card ->
                val text = card.text().trim()
                if (text.isBlank()) {
                    return@forEach
                }

                val price = extractPrice(text) ?: return@forEach
                val title = extractTitle(card) ?: return@forEach

                val href = card.selectFirst("a")?.attr("href")
                val fullHref = normalizeUrl(href, baseUrl)

                offers.add(
                    Offer(
                        site = name,
                        title = title,
                        price = price,
                        sizeText = extractQuantityText(title),
                        url = fullHref,
                        raw = mapOf("source" to "html-card")
                    )
                )
            }
        }

        return offers
    }

    private fun extractTitle(card: Element): String? {
        val attrs = listOf("title", "aria-label", "data-name")
        attrs.forEach { attr ->
            val value = card.attr(attr).trim()
            if (value.length >= 3) {
                return value
            }
        }

        val tags = listOf("h1", "h2", "h3", "h4", "p", "span")
        tags.forEach { tag ->
            val node = card.selectFirst(tag)
            val text = node?.text()?.trim().orEmpty()
            if (text.length >= 3) {
                return text
            }
        }

        return null
    }

    private fun mapProductNode(node: JsonObject): Offer? {
        val title = node.stringByKeys("name", "title", "productName", "product_name")
            ?.takeIf { it.length >= 3 } ?: return null

        val price = node.priceByKeys("price", "salePrice", "sale_price", "final_price", "mrp")
            ?: return null

        val brand = node.stringByKeys("brand")
        val size = node.stringByKeys("size", "quantity", "pack", "pack_size")
        val image = node.firstUrlByKeys("image", "image_url", "thumbnail", "thumbnail_url")
        val url = node.firstUrlByKeys("url", "productUrl", "slug", "absolute_url")

        return Offer(
            site = name,
            title = title,
            price = price,
            brand = brand,
            sizeText = size ?: extractQuantityText(title),
            url = url,
            imageUrl = image,
            raw = mapOf("source" to "json")
        )
    }

    private fun walkProductNodes(element: JsonElement): List<JsonObject> {
        val found = mutableListOf<JsonObject>()

        fun walk(node: JsonElement) {
            when (node) {
                is JsonObject -> {
                    if (node.looksLikeProduct()) {
                        found.add(node)
                    }
                    node.values.forEach { child -> walk(child) }
                }

                is JsonArray -> node.forEach { child -> walk(child) }
                else -> Unit
            }
        }

        walk(element)
        return found
    }

    private fun JsonObject.looksLikeProduct(): Boolean {
        val keys = this.keys.map { it.lowercase() }.toSet()
        val hasName = keys.any { it in setOf("name", "title", "productname", "product_name") }
        val hasPrice = keys.any { it in setOf("price", "saleprice", "sale_price", "mrp", "final_price") }
        return hasName && hasPrice
    }

    private fun JsonObject.stringByKeys(vararg keys: String): String? {
        keys.forEach { key ->
            val value = this[key] ?: return@forEach
            when (value) {
                is JsonPrimitive -> {
                    if (value.isString) {
                        val text = value.content.trim()
                        if (text.isNotBlank()) {
                            return text
                        }
                    } else {
                        val text = value.content.trim()
                        if (text.isNotBlank()) {
                            return text
                        }
                    }
                }

                is JsonObject -> {
                    val candidate = value.stringByKeys("name", "value", "text", "label")
                    if (!candidate.isNullOrBlank()) {
                        return candidate
                    }
                }

                else -> Unit
            }
        }
        return null
    }

    private fun JsonObject.priceByKeys(vararg keys: String): Double? {
        keys.forEach { key ->
            val value = this[key] ?: return@forEach
            val numeric = value.toNumericDouble()
            if (numeric != null && numeric > 0.0) {
                return numeric
            }
        }
        return null
    }

    private fun JsonObject.firstUrlByKeys(vararg keys: String): String? {
        keys.forEach { key ->
            when (val value = this[key]) {
                is JsonPrimitive -> {
                    val content = value.content.trim()
                    if (content.startsWith("http")) {
                        return content
                    }
                }

                is JsonArray -> {
                    value.forEach { item ->
                        val candidate = when (item) {
                            is JsonPrimitive -> item.content
                            is JsonObject -> item.stringByKeys("url", "image", "thumbnail", "m", "l", "xl")
                            else -> null
                        }
                        if (!candidate.isNullOrBlank() && candidate.startsWith("http")) {
                            return candidate
                        }
                    }
                }

                is JsonObject -> {
                    val candidate = value.stringByKeys("url", "image", "thumbnail", "m", "l", "xl")
                    if (!candidate.isNullOrBlank() && candidate.startsWith("http")) {
                        return candidate
                    }
                }

                else -> Unit
            }
        }
        return null
    }

    private fun JsonElement.toNumericDouble(): Double? {
        return when (this) {
            is JsonPrimitive -> {
                if (isString) {
                    extractPrice(content)
                } else {
                    content.toDoubleOrNull()
                }
            }

            is JsonObject -> {
                this["value"]?.toNumericDouble()
                    ?: this["amount"]?.toNumericDouble()
                    ?: this["price"]?.toNumericDouble()
            }

            JsonNull -> null
            else -> null
        }
    }

    protected fun extractPrice(text: String): Double? {
        val regex = "(?:₹|Rs\\.?|INR)\\s*([0-9]+(?:[.,][0-9]{1,2})?)".toRegex(RegexOption.IGNORE_CASE)
        val match = regex.find(text) ?: return null
        val raw = match.groupValues[1].replace(",", "")
        return raw.toDoubleOrNull()
    }

    protected fun dedupeOffers(offers: List<Offer>, maxResults: Int): List<Offer> {
        val seen = mutableSetOf<Pair<String, Double>>()
        val deduped = mutableListOf<Offer>()

        offers.forEach { offer ->
            val key = offer.title.lowercase() to round(offer.price * 100.0) / 100.0
            if (key in seen) {
                return@forEach
            }
            seen.add(key)
            deduped.add(offer)
            if (deduped.size >= maxResults) {
                return@forEach
            }
        }

        return deduped.take(maxResults)
    }

    private fun normalizeUrl(url: String?, baseUrl: String): String? {
        if (url.isNullOrBlank()) {
            return null
        }
        val trimmed = url.trim()
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed
        }
        if (!trimmed.startsWith("/")) {
            return null
        }

        val prefix = when {
            baseUrl.startsWith("https://") -> "https://"
            baseUrl.startsWith("http://") -> "http://"
            else -> "https://"
        }

        val hostPart = baseUrl.removePrefix("https://").removePrefix("http://").substringBefore("/")
        return "$prefix$hostPart$trimmed"
    }

    private fun extractQuantityText(text: String): String? {
        val regex = "(\\d+(?:\\.\\d+)?)\\s*(kg|g|l|ltr|litre|liter|ml|pcs|pc|pack)".toRegex(RegexOption.IGNORE_CASE)
        val match = regex.find(text) ?: return null
        val value = match.groupValues[1]
        var unit = match.groupValues[2].lowercase()
        if (unit == "ltr" || unit == "litre" || unit == "liter") {
            unit = "l"
        }
        if (unit == "pc") {
            unit = "pcs"
        }
        return "$value $unit"
    }
}
