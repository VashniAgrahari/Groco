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
    private val repository: PriceComparisonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompareUiState())
    val uiState: StateFlow<CompareUiState> = _uiState.asStateFlow()
    private val isDebuggable: Boolean = (
        appContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        ) != 0

    private var initializedQuery = false

    fun initializeQuery(query: String) {
        if (initializedQuery || query.isBlank()) {
            return
        }
        initializedQuery = true
        _uiState.update { state -> state.copy(query = query) }
    }

    fun updateQuery(query: String) {
        _uiState.update { state -> state.copy(query = query) }
    }

    fun updateCity(city: String) {
        _uiState.update { state -> state.copy(city = city) }
    }

    fun updateArea(area: String) {
        _uiState.update { state -> state.copy(area = area) }
    }

    fun updatePincode(pincode: String) {
        _uiState.update { state -> state.copy(pincode = pincode) }
    }

    fun updateLatitude(latitude: String) {
        _uiState.update { state -> state.copy(latitude = latitude) }
    }

    fun updateLongitude(longitude: String) {
        _uiState.update { state -> state.copy(longitude = longitude) }
    }

    fun updateMaxResults(maxResultsPerSite: String) {
        _uiState.update { state -> state.copy(maxResultsPerSite = maxResultsPerSite) }
    }

    fun comparePrices() {
        val state = _uiState.value
        if (state.query.isBlank()) {
            _uiState.update { it.copy(error = "Enter an item name to compare", result = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val maxResults = state.maxResultsPerSite.toIntOrNull()?.coerceIn(1, 30) ?: 8
            val result = runCatching {
                repository.compare(
                    CompareRequest(
                        location = UserLocation(
                            city = state.city.ifBlank { null },
                            area = state.area.ifBlank { null },
                            pincode = state.pincode.filter { it.isDigit() }.ifBlank { null },
                            latitude = state.latitude.toDoubleOrNull(),
                            longitude = state.longitude.toDoubleOrNull()
                        ),
                        itemName = state.query,
                        searchTag = null,
                        maxResultsPerSite = maxResults
                    )
                )
            }

            result.onSuccess { compareResult ->
                _uiState.update {
                    it.copy(isLoading = false, error = null, result = compareResult)
                }
            }.onFailure { throwable ->
                if (isDebuggable) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            result = demoResult(state.query)
                        )
                    }
                    return@launch
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message?.ifBlank { null } ?: "Failed to compare prices",
                        result = null
                    )
                }
            }
        }
    }

    private fun demoResult(query: String): CompareResult {
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
            location = UserLocation(),
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
    val city: String = "",
    val area: String = "",
    val pincode: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val maxResultsPerSite: String = "8",
    val isLoading: Boolean = false,
    val error: String? = null,
    val result: CompareResult? = null
)
