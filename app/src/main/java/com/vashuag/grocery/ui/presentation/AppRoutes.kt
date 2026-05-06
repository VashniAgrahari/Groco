package com.vashuag.grocery.ui.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoutes() {

    @Serializable
    data object HomeScreen : AppRoutes()

    @Serializable
    data object ScanItemsScreen : AppRoutes()

    @Serializable
    data class CompareItemScreen(
        val itemName: String
    ) : AppRoutes()
}
