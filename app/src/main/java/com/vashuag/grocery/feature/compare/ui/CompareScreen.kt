package com.vashuag.grocery.feature.compare.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vashuag.grocery.feature.compare.domain.MatchedItem
import com.vashuag.grocery.feature.compare.domain.Offer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CompareScreen(
    initialQuery: String,
    viewModel: CompareViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(initialQuery) {
        viewModel.initializeQuery(initialQuery)
    }

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Compare Prices",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::updateQuery,
                label = { Text("Item name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("compare_query_input"),
                singleLine = true
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.city,
                    onValueChange = viewModel::updateCity,
                    label = { Text("City") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.pincode,
                    onValueChange = viewModel::updatePincode,
                    label = { Text("Pincode") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.area,
                    onValueChange = viewModel::updateArea,
                    label = { Text("Area") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.maxResultsPerSite,
                    onValueChange = viewModel::updateMaxResults,
                    label = { Text("Max/site") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.latitude,
                    onValueChange = viewModel::updateLatitude,
                    label = { Text("Latitude") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.longitude,
                    onValueChange = viewModel::updateLongitude,
                    label = { Text("Longitude") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }

        item {
            Button(
                onClick = viewModel::comparePrices,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("compare_search_button")
            ) {
                Text("Compare")
            }
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        }

        state.error?.let { error ->
            item {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        state.result?.let { result ->
            item {
                Text(
                    text = "Results for ${result.query}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.testTag("compare_results_header")
                )
            }
            item {
                Text(
                    text = "Updated ${formatTimestamp(result.generatedAtMs)}${if (result.fromCache) " • cached" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(result.matchedItems) { matched ->
                MatchedItemCard(
                    matchedItem = matched,
                    onOpenUrl = { url ->
                        runCatching { uriHandler.openUri(url) }
                    }
                )
            }
        }
    }
}

@Composable
private fun MatchedItemCard(
    matchedItem: MatchedItem,
    onOpenUrl: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = matchedItem.canonicalName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Confidence ${(matchedItem.confidence * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            matchedItem.offers.forEach { offer ->
                OfferRow(offer = offer, onOpenUrl = onOpenUrl)
            }
        }
    }
}

@Composable
private fun OfferRow(
    offer: Offer,
    onOpenUrl: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${offer.site}: ${offer.title}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "₹${"%.2f".format(offer.price)}${offer.sizeText?.let { " • $it" } ?: ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (!offer.url.isNullOrBlank()) {
            Button(
                onClick = { onOpenUrl(offer.url) }
            ) {
                Text("Open")
            }
        }
    }
}

private fun formatTimestamp(timestampMs: Long): String {
    val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return formatter.format(Date(timestampMs))
}
