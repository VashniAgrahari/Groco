package com.vashuag.grocery.ui.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.graphics.BitmapFactory
import android.content.pm.ApplicationInfo
import com.vashuag.grocery.data.entity.GroceryItem
import com.vashuag.grocery.feature.compare.ui.CompareScreen
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenLocationSettings: () -> Unit = {}
) {
    val groceryItems by viewModel.groceryItems.collectAsState()
    val isDebuggable = (
        androidx.compose.ui.platform.LocalContext.current.applicationInfo.flags and
            ApplicationInfo.FLAG_DEBUGGABLE
        ) != 0
    val compareItemState = remember { mutableStateOf<GroceryItem?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Grocery Items",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag("home_title")
            )
            FilledTonalButton(
                onClick = { viewModel.refreshGroceryItems() },
                modifier = Modifier.testTag("home_refresh_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Refresh")
            }
        }

        if (isDebuggable) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = { viewModel.seedDemoItems() },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("debug_seed_demo_items")
                ) {
                    Text("Load Demo Data")
                }
                FilledTonalButton(
                    onClick = { viewModel.clearAllItems() },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("debug_clear_items")
                ) {
                    Text("Clear All")
                }
            }
        }

        if (groceryItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No grocery items yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(groceryItems, key = { it.id }) { item ->
                    GroceryItemCard(
                        item = item,
                        onDeleteClick = { viewModel.deleteGroceryItem(item) },
                        onIncrementQuantity = { viewModel.incrementQuantity(item.id) },
                        onDecrementQuantity = { viewModel.decrementQuantity(item.id) },
                        onCompareClick = { compareItemState.value = item }
                    )
                }
            }
        }
    }

    compareItemState.value?.let { selectedItem ->
        ModalBottomSheet(
            onDismissRequest = { compareItemState.value = null },
            modifier = Modifier.testTag("compare_bottom_sheet")
        ) {
            CompareScreen(
                initialQuery = selectedItem.title,
                showTitle = false,
                autoCompare = true,
                onOpenLocationSettings = {
                    compareItemState.value = null
                    onOpenLocationSettings()
                }
            )
        }
    }
}

@Composable
fun GroceryItemCard(
    item: GroceryItem,
    onDeleteClick: () -> Unit,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
    onCompareClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Image
            val imageFile = File(item.imagePath)
            if (imageFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(item.imagePath)
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                // Placeholder if image doesn't exist
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Image",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.title.ifEmpty { "Unnamed Item" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Expiry: ${formatDate(item.expiryDateMs)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = if (item.quantity <= item.lowStockThreshold) {
                        "Low stock"
                    } else {
                        "In stock"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (item.quantity <= item.lowStockThreshold) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onDecrementQuantity,
                        modifier = Modifier.testTag("quantity_decrease_button")
                    ) {
                        Text("-")
                    }
                    Text(
                        text = "Qty ${formatQuantity(item.quantity)} ${item.unit}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(
                        onClick = onIncrementQuantity,
                        modifier = Modifier.testTag("quantity_increase_button")
                    ) {
                        Text("+")
                    }
                }

                OutlinedButton(
                    onClick = onCompareClick,
                    modifier = Modifier.testTag("compare_item_button")
                ) {
                    Text("Compare Prices")
                }
            }

            // Delete button
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatQuantity(quantity: Double): String {
    return if (quantity % 1.0 == 0.0) {
        quantity.toInt().toString()
    } else {
        String.format(Locale.getDefault(), "%.1f", quantity)
    }
}
