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
}

