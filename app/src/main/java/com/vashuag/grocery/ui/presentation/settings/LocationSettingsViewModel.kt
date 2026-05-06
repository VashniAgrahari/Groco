package com.vashuag.grocery.ui.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vashuag.grocery.feature.settings.data.LocationSettingsRepository
import com.vashuag.grocery.feature.settings.data.SavedLocationSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSettingsViewModel @Inject constructor(
    private val repository: LocationSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationSettingsUiState())
    val uiState: StateFlow<LocationSettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    fun updateCity(value: String) {
        _uiState.update { it.copy(city = value, saved = false, error = null) }
    }

    fun updateArea(value: String) {
        _uiState.update { it.copy(area = value, saved = false, error = null) }
    }

    fun updatePincode(value: String) {
        _uiState.update {
            it.copy(
                pincode = value.filter { ch -> ch.isDigit() }.take(6),
                saved = false,
                error = null
            )
        }
    }

    fun updateLatitude(value: String) {
        _uiState.update { it.copy(latitude = value, saved = false, error = null) }
    }

    fun updateLongitude(value: String) {
        _uiState.update { it.copy(longitude = value, saved = false, error = null) }
    }

    fun updateMaxResults(value: String) {
        _uiState.update {
            it.copy(
                maxResultsPerSite = value.filter { ch -> ch.isDigit() }.take(2),
                saved = false,
                error = null
            )
        }
    }

    fun saveSettings() {
        val state = _uiState.value
        val maxResults = state.maxResultsPerSite.toIntOrNull()
        if (maxResults == null || maxResults !in 1..30) {
            _uiState.update { it.copy(error = "Max/site must be between 1 and 30", saved = false) }
            return
        }

        val lat = state.latitude.trim().takeIf { it.isNotEmpty() }?.toDoubleOrNull()
        val lng = state.longitude.trim().takeIf { it.isNotEmpty() }?.toDoubleOrNull()
        val hasAnyCoordinate = state.latitude.isNotBlank() || state.longitude.isNotBlank()
        if (hasAnyCoordinate && (lat == null || lng == null)) {
            _uiState.update { it.copy(error = "Enter both latitude and longitude", saved = false) }
            return
        }

        repository.saveSettings(
            SavedLocationSettings(
                city = state.city.trim(),
                area = state.area.trim(),
                pincode = state.pincode.trim(),
                latitude = lat,
                longitude = lng,
                maxResultsPerSite = maxResults
            )
        )
        _uiState.update { it.copy(error = null, saved = true) }
    }

    fun resetDefaults() {
        repository.resetDefaults()
        loadSettings()
        _uiState.update { it.copy(error = null, saved = true) }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val settings = repository.getSavedSettings()
            _uiState.update {
                it.copy(
                    city = settings.city,
                    area = settings.area,
                    pincode = settings.pincode,
                    latitude = settings.latitude?.toString() ?: "",
                    longitude = settings.longitude?.toString() ?: "",
                    maxResultsPerSite = settings.maxResultsPerSite.toString(),
                    error = null
                )
            }
        }
    }
}

data class LocationSettingsUiState(
    val city: String = "",
    val area: String = "",
    val pincode: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val maxResultsPerSite: String = "8",
    val error: String? = null,
    val saved: Boolean = false
)
