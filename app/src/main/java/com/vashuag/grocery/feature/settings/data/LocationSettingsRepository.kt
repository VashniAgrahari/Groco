package com.vashuag.grocery.feature.settings.data

import com.vashuag.grocery.data.entity.LocationSettings
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

data class SavedLocationSettings(
    val city: String = "",
    val area: String = "",
    val pincode: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val maxResultsPerSite: Int = 8
)

@Singleton
class LocationSettingsRepository @Inject constructor(
    private val settingsBox: Box<LocationSettings>
) {

    fun getSavedSettings(): SavedLocationSettings {
        val entity = getOrCreateEntity()
        return SavedLocationSettings(
            city = entity.city.trim(),
            area = entity.area.trim(),
            pincode = entity.pincode.trim(),
            latitude = if (entity.hasCoordinates) entity.latitude else null,
            longitude = if (entity.hasCoordinates) entity.longitude else null,
            maxResultsPerSite = entity.maxResultsPerSite.coerceIn(1, 30)
        )
    }

    fun saveSettings(settings: SavedLocationSettings) {
        val entity = getOrCreateEntity()
        entity.city = settings.city.trim()
        entity.area = settings.area.trim()
        entity.pincode = settings.pincode.filter { it.isDigit() }.take(6)
        entity.latitude = settings.latitude ?: 0.0
        entity.longitude = settings.longitude ?: 0.0
        entity.hasCoordinates = settings.latitude != null && settings.longitude != null
        entity.maxResultsPerSite = settings.maxResultsPerSite.coerceIn(1, 30)
        settingsBox.put(entity)
    }

    fun resetDefaults() {
        val defaults = defaultEntity()
        val entity = getOrCreateEntity()
        entity.city = defaults.city
        entity.area = defaults.area
        entity.pincode = defaults.pincode
        entity.latitude = defaults.latitude
        entity.longitude = defaults.longitude
        entity.hasCoordinates = defaults.hasCoordinates
        entity.maxResultsPerSite = defaults.maxResultsPerSite
        settingsBox.put(entity)
    }

    private fun defaultEntity(): LocationSettings {
        return LocationSettings(
            city = "Bengaluru",
            area = "Indiranagar",
            pincode = "560038",
            latitude = 12.9716,
            longitude = 77.5946,
            hasCoordinates = true,
            maxResultsPerSite = 8
        )
    }

    private fun getOrCreateEntity(): LocationSettings {
        settingsBox.all.firstOrNull()?.let { return it }
        val entity = defaultEntity()
        settingsBox.put(entity)
        return entity
    }
}
