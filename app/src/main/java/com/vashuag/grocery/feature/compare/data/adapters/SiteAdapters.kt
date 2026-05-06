package com.vashuag.grocery.feature.compare.data.adapters

import com.vashuag.grocery.feature.compare.domain.Offer
import com.vashuag.grocery.feature.compare.domain.UserLocation
import com.vashuag.grocery.feature.compare.domain.normalizeText
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.round

class BlinkitAdapter(httpClient: HttpClient) : GenericEcommerceAdapter(httpClient) {
    override val name: String = "blinkit"
    override val searchUrlTemplate: String = "https://blinkit.com/s/?q={query}"
}

class SwiggyInstamartAdapter(httpClient: HttpClient) : GenericEcommerceAdapter(httpClient) {
    override val name: String = "swiggy_instamart"
    override val searchUrlTemplate: String = "https://www.swiggy.com/instamart/search?query={query}"
}

class ZeptoAdapter(httpClient: HttpClient) : GenericEcommerceAdapter(httpClient) {
    override val name: String = "zepto"
    override val searchUrlTemplate: String = "https://www.zeptonow.com/search?query={query}"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun search(query: String, location: UserLocation, maxResults: Int): List<Offer> {
        if (query.isBlank()) {
            return emptyList()
        }

        val encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        val url = searchUrlTemplate.replace("{query}", encodedQuery)

        val response = runCatching {
            httpClient.get(url) {
                header("user-agent", userAgent)
                header("accept-language", "en-IN,en;q=0.9")
                header("accept", "text/html,application/xhtml+xml")
            }
        }.getOrNull() ?: return emptyList()

        if (response.status.value !in 200..299) {
            return emptyList()
        }

        val html = runCatching { response.bodyAsText() }.getOrNull() ?: return emptyList()
        val offers = mutableListOf<Offer>()

        for (card in extractCardDataObjects(html)) {
            val product = card.objectByKey("product") ?: continue
            val variant = card.objectByKey("productVariant")

            val title = cleanTitle(product.stringByKey("name"))
            if (title.length < 3) {
                continue
            }

            val paise = card.doubleByKeys("discountedSellingPrice", "sellingPrice") ?: 0.0
            if (paise <= 0.0) {
                continue
            }
            val price = round((paise / 100.0) * 100.0) / 100.0

            val slug = product.stringByKey("slug")
            val offerUrl = if (!slug.isNullOrBlank()) {
                "https://www.zeptonow.com/pn/$slug"
            } else {
                "https://www.zeptonow.com/search?query=${URLEncoder.encode(title, StandardCharsets.UTF_8.toString())}"
            }

            val imageUrl = variant?.arrayByKey("images")
                ?.firstOrNull()
                ?.let { it as? JsonObject }
                ?.stringByKey("path")
                ?.takeIf { it.isNotBlank() }
                ?.let { path -> "https://cdn.zeptonow.com/${path.trimStart('/')}" }

            val brand = product.stringByKey("brand") ?: title.substringBefore(" ")
            val size = variant?.stringByKey("formattedPacksize") ?: extractQuantityTextLocal(title)

            offers += Offer(
                site = name,
                title = title,
                price = price,
                brand = brand,
                sizeText = size,
                inStock = card.booleanByKey("isActive") ?: true,
                url = offerUrl,
                imageUrl = imageUrl,
                raw = mapOf("source" to "zepto-html-cardData")
            )
        }

        return dedupeOffers(offers, maxResults)
    }

    private fun extractCardDataObjects(payload: String): List<JsonObject> {
        val needle = "\\\"cardData\\\":{"
        var position = 0
        val cards = mutableListOf<JsonObject>()

        while (true) {
            val index = payload.indexOf(needle, startIndex = position)
            if (index == -1) {
                break
            }

            val start = payload.indexOf('{', startIndex = index + needle.length - 1)
            if (start == -1) {
                break
            }

            var depth = 0
            var end = -1
            for (cursor in start until payload.length) {
                when (payload[cursor]) {
                    '{' -> depth += 1
                    '}' -> {
                        depth -= 1
                        if (depth == 0) {
                            end = cursor
                            break
                        }
                    }
                }
            }

            if (end == -1) {
                break
            }

            val rawObject = payload.substring(start, end + 1)
            position = end + 1

            val normalized = rawObject
                .replace("\\\"", "\"")
                .replace("\\/", "/")

            val parsed = runCatching {
                json.parseToJsonElement(normalized) as? JsonObject
            }.getOrNull() ?: continue

            cards += parsed
        }

        return cards
    }

    private fun cleanTitle(title: String?): String {
        if (title.isNullOrBlank()) {
            return ""
        }
        return title.replace("\\s+".toRegex(), " ").trim()
    }

    private fun extractQuantityTextLocal(text: String): String? {
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

    private fun JsonObject.objectByKey(key: String): JsonObject? {
        return this[key] as? JsonObject
    }

    private fun JsonObject.arrayByKey(key: String): JsonArray? {
        return this[key] as? JsonArray
    }

    private fun JsonObject.stringByKey(key: String): String? {
        val primitive = this[key] as? JsonPrimitive ?: return null
        val value = primitive.content.trim()
        return value.takeIf { it.isNotBlank() }
    }

    private fun JsonObject.booleanByKey(key: String): Boolean? {
        val primitive = this[key] as? JsonPrimitive ?: return null
        return when (primitive.content.trim().lowercase()) {
            "true" -> true
            "false" -> false
            else -> null
        }
    }

    private fun JsonObject.doubleByKeys(vararg keys: String): Double? {
        for (key in keys) {
            val numeric = (this[key] as? JsonPrimitive)?.content?.replace(",", "")?.toDoubleOrNull()
            if (numeric != null && numeric > 0.0) {
                return numeric
            }
        }
        return null
    }
}

class AmazonNowAdapter(httpClient: HttpClient) : GenericEcommerceAdapter(httpClient) {
    override val name: String = "amazon_now"
    override val searchUrlTemplate: String = "https://www.amazon.in/s?k={query}&i=nowstore"

    override suspend fun search(query: String, location: UserLocation, maxResults: Int): List<Offer> {
        if (query.isBlank()) {
            return emptyList()
        }
        val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        val url = searchUrlTemplate.replace("{query}", encoded)
        val response = runCatching {
            httpClient.get(url) {
                header("user-agent", userAgent)
                header("accept-language", "en-IN,en;q=0.9")
                header("accept", "text/html,application/xhtml+xml")
            }
        }.getOrNull() ?: return emptyList()

        if (response.status.value !in 200..299) {
            return emptyList()
        }

        val html = runCatching { response.bodyAsText() }.getOrNull() ?: return emptyList()
        val soup = Jsoup.parse(html)
        val cards = soup.select("div[data-component-type=s-search-result]")
        val offers = mutableListOf<Offer>()

        cards.forEach { card ->
            val text = card.text().trim()
            if (text.isBlank()) {
                return@forEach
            }

            val title = cleanTitle(extractTitle(card))
            if (title.length < 3) {
                return@forEach
            }

            val priceText = card.selectFirst("span.a-price span.a-offscreen")?.text()
            val price = extractPrice(priceText ?: text) ?: return@forEach

            val href = card.selectFirst("a[href*=/dp/]")?.attr("href")
                ?: card.selectFirst("h2 a")?.attr("href")
            val urlValue = when {
                href.isNullOrBlank() -> "https://www.amazon.in/s?k=${URLEncoder.encode(title, StandardCharsets.UTF_8.toString())}&i=nowstore"
                href.startsWith("/") -> "https://www.amazon.in$href"
                else -> href
            }
            val image = card.selectFirst("img.s-image")?.attr("src")

            offers += Offer(
                site = name,
                title = title,
                price = price,
                brand = title.substringBefore(" "),
                sizeText = extractQuantityTextLocal(title),
                inStock = !text.contains("currently unavailable", ignoreCase = true),
                url = urlValue,
                imageUrl = image?.takeIf { it.isNotBlank() },
                raw = mapOf("source" to "amazon-search-card")
            )
        }

        val relevant = offers.filter { offer -> isRelevant(query, offer.title) }
        return dedupeOffers(relevant.ifEmpty { offers }, maxResults)
    }

    private fun extractTitle(card: Element): String {
        val heading = card.selectFirst("h2")?.text()?.trim().orEmpty()
        if (heading.isNotBlank()) {
            return heading
        }
        return card.selectFirst("h2 span")?.text()?.trim()
            ?: card.selectFirst("h2 a")?.text()?.trim()
            ?: ""
    }

    private fun cleanTitle(text: String): String {
        return text.replace("\\s+".toRegex(), " ").trim()
    }

    private fun extractQuantityTextLocal(text: String): String? {
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

    private fun isRelevant(query: String, title: String): Boolean {
        val queryTokens = normalizeText(query).split(" ").filter { it.length > 1 }
        if (queryTokens.isEmpty()) {
            return true
        }
        val normalizedTitle = normalizeText(title)
        return queryTokens.any { token -> token in normalizedTitle }
    }
}

class JioMartAdapter(httpClient: HttpClient) : GenericEcommerceAdapter(httpClient) {
    override val name: String = "jiomart"
    override val searchUrlTemplate: String = "https://www.jiomart.com/search/{query}"
}

class BigBasketAdapter(httpClient: HttpClient) : GenericEcommerceAdapter(httpClient) {
    override val name: String = "bigbasket"
    override val searchUrlTemplate: String = "https://www.bigbasket.com/ps/?q={query}"
}

class FlipkartMinutesAdapter(httpClient: HttpClient) : GenericEcommerceAdapter(httpClient) {
    override val name: String = "flipkart_minutes"
    override val searchUrlTemplate: String = "https://www.flipkart.com/search?q={query}&marketplace=GROCERY"
}
