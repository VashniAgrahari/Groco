package com.vashuag.grocery.ui.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun LocationSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: LocationSettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Location Settings",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            OutlinedTextField(
                value = state.city,
                onValueChange = viewModel::updateCity,
                label = { Text("City") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("location_city_input"),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = state.area,
                onValueChange = viewModel::updateArea,
                label = { Text("Area") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("location_area_input"),
                singleLine = true
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.pincode,
                    onValueChange = viewModel::updatePincode,
                    label = { Text("Pincode") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("location_pincode_input"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.maxResultsPerSite,
                    onValueChange = viewModel::updateMaxResults,
                    label = { Text("Max/site") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("location_max_results_input"),
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
                    modifier = Modifier
                        .weight(1f)
                        .testTag("location_lat_input"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.longitude,
                    onValueChange = viewModel::updateLongitude,
                    label = { Text("Longitude") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("location_lng_input"),
                    singleLine = true
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = viewModel::saveSettings,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("location_save_button")
                ) {
                    Text("Save")
                }
                OutlinedButton(
                    onClick = viewModel::resetDefaults,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("location_reset_button")
                ) {
                    Text("Reset")
                }
            }
        }

        state.error?.let { error ->
            item {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("location_error_text")
                )
            }
        }

        if (state.saved && state.error == null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "Saved",
                        modifier = Modifier
                            .padding(12.dp)
                            .testTag("location_saved_text"),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}
