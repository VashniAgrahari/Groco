package com.vashuag.grocery.feature.compare.data

import com.vashuag.grocery.data.entity.ComparisonHistory
import com.vashuag.grocery.feature.compare.data.adapters.AmazonNowAdapter
import com.vashuag.grocery.feature.compare.data.adapters.BigBasketAdapter
import com.vashuag.grocery.feature.compare.data.adapters.BlinkitAdapter
import com.vashuag.grocery.feature.compare.data.adapters.FlipkartMinutesAdapter
import com.vashuag.grocery.feature.compare.data.adapters.JioMartAdapter
import com.vashuag.grocery.feature.compare.data.adapters.PriceSourceAdapter
import com.vashuag.grocery.feature.compare.data.adapters.SwiggyInstamartAdapter
import com.vashuag.grocery.feature.compare.data.adapters.ZeptoAdapter
import com.vashuag.grocery.feature.compare.domain.OfferMatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.objectbox.Box
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompareModule {

    @Provides
    @Singleton
    fun provideOfferMatcher(): OfferMatcher {
        return OfferMatcher()
    }

    @Provides
    @Singleton
    fun providePriceSourceAdapters(
        httpClient: HttpClient
    ): List<PriceSourceAdapter> {
        return listOf(
            BlinkitAdapter(httpClient),
            SwiggyInstamartAdapter(httpClient),
            ZeptoAdapter(httpClient),
            AmazonNowAdapter(httpClient),
            JioMartAdapter(httpClient),
            BigBasketAdapter(httpClient),
            FlipkartMinutesAdapter(httpClient)
        )
    }

    @Provides
    @Singleton
    fun providePriceComparisonRepository(
        adapters: List<@JvmSuppressWildcards PriceSourceAdapter>,
        matcher: OfferMatcher,
        historyBox: Box<ComparisonHistory>
    ): PriceComparisonRepository {
        return PriceComparisonRepository(adapters, matcher, historyBox)
    }
}
