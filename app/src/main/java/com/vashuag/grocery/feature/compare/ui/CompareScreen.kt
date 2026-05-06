package com.vashuag.grocery.feature.compare.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.vashuag.grocery.feature.compare.domain.MatchedItem
import com.vashuag.grocery.feature.compare.domain.Offer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CompareScreen(
    initialQuery: String,
    modifier: Modifier = Modifier,
    showTitle: Boolean = true,
    autoCompare: Boolean = false,
    onOpenLocationSettings: () -> Unit = {},
    viewModel: CompareViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(initialQuery, autoCompare) {
        viewModel.refreshLocationConfig()
        if (initialQuery.isNotBlank()) {
            viewModel.initializeQuery(initialQuery, autoCompare = autoCompare)
        }
    }

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (showTitle) {
            item {
                Text(
                    text = "Compare Prices",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.locationSummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("compare_location_summary"),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    OutlinedButton(
                        onClick = onOpenLocationSettings,
                        modifier = Modifier.testTag("compare_location_button")
                    ) {
                        Text("Location")
                    }
                }
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

            if (result.matchedItems.isEmpty()) {
                item {
                    Text(
                        text = "No offers found right now.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
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
                OfferCard(offer = offer, onOpenUrl = onOpenUrl)
            }
        }
    }
}

@Composable
private fun OfferCard(
    offer: Offer,
    onOpenUrl: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!offer.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = offer.imageUrl,
                        contentDescription = offer.title,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = offer.site.take(1).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = offer.site.replace("_", " ").uppercase(Locale.getDefault()),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Text(
                            text = "₹${"%.2f".format(offer.price)}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = offer.title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!offer.sizeText.isNullOrBlank()) {
                        Text(
                            text = offer.sizeText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (!offer.url.isNullOrBlank()) {
                FilledTonalButton(
                    onClick = { onOpenUrl(offer.url) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Open")
                }
            }
        }
    }
}

private fun formatTimestamp(timestampMs: Long): String {
    val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return formatter.format(Date(timestampMs))
}
