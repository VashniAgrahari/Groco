package com.vashuag.grocery.data.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class LocationSettings(
    @Id var id: Long = 0L,
    var city: String = "",
    var area: String = "",
    var pincode: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var hasCoordinates: Boolean = false,
    var maxResultsPerSite: Int = 8
)
