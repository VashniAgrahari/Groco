package com.vashuag.grocery.ui.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vashuag.grocery.data.entity.GroceryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.objectbox.Box
import io.objectbox.android.AndroidScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val groceryBox: Box<GroceryItem>
) : ViewModel() {

    private val _groceryItems = MutableStateFlow<List<GroceryItem>>(emptyList())
    val groceryItems: StateFlow<List<GroceryItem>> = _groceryItems.asStateFlow()

    init {
        subscribeToGroceryItems()
    }

    private fun subscribeToGroceryItems() {
        // Query all grocery items and observe changes in real-time
        val query = groceryBox.query().build()

        // Subscribe to data changes on Android main thread
        query.subscribe()
            .on(AndroidScheduler.mainThread())
            .observer { data ->
                viewModelScope.launch {
                    _groceryItems.value = data
                }
            }
    }

    fun refreshGroceryItems() {
        viewModelScope.launch {
            // Manually fetch all items from database
            val items = groceryBox.all
            _groceryItems.value = items
        }
    }

    fun deleteGroceryItem(item: GroceryItem) {
        viewModelScope.launch {
            groceryBox.remove(item)
            // Manually refresh after deletion
            refreshGroceryItems()
        }
    }

    fun incrementQuantity(itemId: Long) {
        viewModelScope.launch {
            val existing = groceryBox.get(itemId) ?: return@launch
            existing.quantity = (existing.quantity + 1.0).coerceAtMost(999.0)
            groceryBox.put(existing)
            refreshGroceryItems()
        }
    }

    fun decrementQuantity(itemId: Long) {
        viewModelScope.launch {
            val existing = groceryBox.get(itemId) ?: return@launch
            existing.quantity = (existing.quantity - 1.0).coerceAtLeast(0.0)
            groceryBox.put(existing)
            refreshGroceryItems()
        }
    }

    fun clearAllItems() {
        viewModelScope.launch {
            groceryBox.removeAll()
            refreshGroceryItems()
        }
    }

    fun seedDemoItems() {
        viewModelScope.launch {
            val soonExpiry = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 2) }.timeInMillis
            val mediumExpiry = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 7) }.timeInMillis
            val longExpiry = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 20) }.timeInMillis

            val demoItems = listOf(
                GroceryItem(
                    title = "Amul Gold Milk",
                    imagePath = "",
                    expiryDateMs = soonExpiry
                ),
                GroceryItem(
                    title = "Fortune Basmati Rice",
                    imagePath = "",
                    expiryDateMs = mediumExpiry
                ),
                GroceryItem(
                    title = "Whole Wheat Bread",
                    imagePath = "",
                    expiryDateMs = longExpiry
                )
            )

            groceryBox.put(*demoItems.toTypedArray())
            refreshGroceryItems()
        }
    }
}
