package com.vashuag.grocery.feature.compare.data.adapters

import io.ktor.client.HttpClient

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
}

class AmazonNowAdapter(httpClient: HttpClient) : GenericEcommerceAdapter(httpClient) {
    override val name: String = "amazon_now"
    override val searchUrlTemplate: String = "https://www.amazon.in/s?k={query}&i=nowstore"
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
