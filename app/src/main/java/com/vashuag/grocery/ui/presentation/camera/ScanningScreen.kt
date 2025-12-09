package com.vashuag.grocery.ui.presentation.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch

@Composable
fun ScanningScreen(
    viewModel: MainViewModel = hiltViewModel<MainViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val state = viewModel.scanningState
    LaunchedEffect(lifecycleOwner) {
        viewModel.initializeCamera(lifecycleOwner)
    }

    if (state.isScanning) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is CameraUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is CameraUiState.Ready -> {
                    AndroidView(
                        factory = { (uiState as CameraUiState.Ready).previewView },
                        modifier = Modifier.fillMaxSize()
                    )
                    AndroidView(
                        factory = { (uiState as CameraUiState.Ready).graphicOverlay },
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .padding(bottom = 50.dp)
                                .size(90.dp)
                                .border(
                                    width = 6.dp, color = Color.White, shape = CircleShape
                                )
                                .padding(6.dp)
                                .border(
                                    width = 4.dp, color = Color.White, shape = CircleShape
                                )
                                .padding(6.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                                .clickable {
                                    viewModel.captureCurrentImage()
                                })
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.End
                    ) {
                        Text(if (state.currentStep == ScanStep.FRONT_IMAGE) "Front" else "Expiry Date")
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            Modifier.fillMaxWidth()
                        ) {
                            Spacer(Modifier.weight(1f))
                            Button(onClick = {
                                viewModel.stopScanning()
                            }, enabled = state.currentStep == ScanStep.FRONT_IMAGE) {
                                Text("Complete Scan")
                            }
                        }
                    }
                }

                is CameraUiState.Error -> {
                    Text(
                        text = "Error: ${(uiState as CameraUiState.Error).message}",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Total Scans: ${state.detectedObjects.size}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (state.detectedObjects.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No objects detected yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {
                val coroutineScope = rememberCoroutineScope()
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(state.detectedObjects) { index, detectedObject ->
                        DetectedObjectCard(
                            index = index,
                            detectedObject = detectedObject,
                            modifier = Modifier.padding(bottom = 12.dp))
                    }
                }
            }

            Button(
                onClick = { viewModel.startScanning() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Continue Scanning")
            }
        }
    }
}

@Composable
fun DetectedObjectCard(
    index: Int,
    detectedObject: DetectedObjectImage,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display the cropped image
            Image(
                bitmap = detectedObject.bitmap.asImageBitmap(),
                contentDescription = "Detected object ${index + 1}",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Display metadata
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Object #${index + 1}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (detectedObject.labels.isNotEmpty()) {
                    Text(
                        text = "Labels: ${detectedObject.labels.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Confidence: ${
                            detectedObject.confidence.joinToString(", ") {
                                "%.1f%%".format(
                                    it * 100
                                )
                            }
                        }", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                } else {
                    Text(
                        text = "No labels available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Size: ${detectedObject.bitmap.width} × ${detectedObject.bitmap.height}px",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "${detectedObject.boundingBox}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                detectedObject.trackingId?.let { trackingId ->
                    Text(
                        text = "Tracking ID: $trackingId",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}