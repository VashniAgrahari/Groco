package com.vashuag.grocery.feature.compare.ui

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vashuag.grocery.feature.compare.data.PriceComparisonRepository
import com.vashuag.grocery.feature.compare.domain.CompareRequest
import com.vashuag.grocery.feature.compare.domain.CompareResult
import com.vashuag.grocery.feature.compare.domain.MatchedItem
import com.vashuag.grocery.feature.compare.domain.Offer
import com.vashuag.grocery.feature.compare.domain.UserLocation
import com.vashuag.grocery.feature.settings.data.LocationSettingsRepository
import com.vashuag.grocery.feature.settings.data.SavedLocationSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val repository: PriceComparisonRepository,
    private val locationSettingsRepository: LocationSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompareUiState())
    val uiState: StateFlow<CompareUiState> = _uiState.asStateFlow()
    private val isDebuggable: Boolean = (
        appContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        ) != 0
    init {
        refreshLocationConfig()
    }

    fun initializeQuery(query: String, autoCompare: Boolean = false) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            return
        }
        _uiState.update { state -> state.copy(query = trimmed) }
        if (autoCompare) {
            comparePrices()
        }
    }

    fun updateQuery(query: String) {
        _uiState.update { state -> state.copy(query = query) }
    }

    fun clearResult() {
        _uiState.update { state -> state.copy(error = null, result = null) }
    }

    fun refreshLocationConfig() {
        val saved = locationSettingsRepository.getSavedSettings()
        _uiState.update { state ->
            state.copy(
                locationSummary = formatLocationSummary(saved),
                maxResultsPerSite = saved.maxResultsPerSite
            )
        }
    }

    fun comparePrices() {
        val query = _uiState.value.query.trim()
        if (query.isBlank()) {
            _uiState.update { it.copy(error = "Enter an item name to compare", result = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val saved = locationSettingsRepository.getSavedSettings()
            val result = runCatching {
                repository.compare(
                    CompareRequest(
                        location = saved.toUserLocation(),
                        itemName = query,
                        searchTag = null,
                        maxResultsPerSite = saved.maxResultsPerSite
                    )
                )
            }

            result.onSuccess { compareResult ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        result = compareResult,
                        locationSummary = formatLocationSummary(saved),
                        maxResultsPerSite = saved.maxResultsPerSite
                    )
                }
            }.onFailure { throwable ->
                if (isDebuggable) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            locationSummary = formatLocationSummary(saved),
                            maxResultsPerSite = saved.maxResultsPerSite,
                            result = demoResult(query, saved)
                        )
                    }
                    return@launch
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message?.ifBlank { null } ?: "Failed to compare prices",
                        locationSummary = formatLocationSummary(saved),
                        maxResultsPerSite = saved.maxResultsPerSite,
                        result = null
                    )
                }
            }
        }
    }

    private fun SavedLocationSettings.toUserLocation(): UserLocation {
        return UserLocation(
            city = city.ifBlank { null },
            area = area.ifBlank { null },
            pincode = pincode.filter { it.isDigit() }.ifBlank { null },
            latitude = latitude,
            longitude = longitude
        )
    }

    private fun formatLocationSummary(settings: SavedLocationSettings): String {
        val locationParts = listOfNotNull(
            settings.area.ifBlank { null },
            settings.city.ifBlank { null },
            settings.pincode.ifBlank { null }
        )
        val locationText = if (locationParts.isEmpty()) {
            "Location not set"
        } else {
            locationParts.joinToString(", ")
        }
        return "$locationText · max ${settings.maxResultsPerSite}/site"
    }

    private fun demoResult(query: String, settings: SavedLocationSettings): CompareResult {
        val offers = listOf(
            Offer(
                site = "blinkit",
                title = "$query 500 ml",
                price = 62.0,
                sizeText = "500 ml",
                url = "https://blinkit.com/"
            ),
            Offer(
                site = "zepto",
                title = "$query 500 ml",
                price = 59.0,
                sizeText = "500 ml",
                url = "https://www.zeptonow.com/"
            ),
            Offer(
                site = "swiggy_instamart",
                title = "$query 500 ml",
                price = 61.0,
                sizeText = "500 ml",
                url = "https://www.swiggy.com/instamart"
            )
        )
        return CompareResult(
            query = query,
            location = settings.toUserLocation(),
            matchedItems = listOf(
                MatchedItem(
                    canonicalName = "$query 500 ml",
                    confidence = 0.93,
                    offers = offers.sortedBy { offer -> offer.price }
                )
            ),
            generatedAtMs = System.currentTimeMillis(),
            fromCache = false,
            rawSiteResults = offers.groupBy { offer -> offer.site },
            siteErrors = emptyMap()
        )
    }
}

data class CompareUiState(
    val query: String = "",
    val locationSummary: String = "",
    val maxResultsPerSite: Int = 8,
    val isLoading: Boolean = false,
    val error: String? = null,
    val result: CompareResult? = null
)
