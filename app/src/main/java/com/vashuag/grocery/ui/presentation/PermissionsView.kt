package com.vashuag.grocery.ui.presentation

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsView(
    content: @Composable () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = buildList {
            add(Manifest.permission.CAMERA)
            add(Manifest.permission.RECORD_AUDIO)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    )

    when {
        permissionState.allPermissionsGranted -> {
            content()
        }

        permissionState.shouldShowRationale -> {
            Column(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {
                Text(
                    text = "Camera, audio, and notification permissions are required for full app functionality.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.Companion.padding(bottom = 16.dp)
                )
                Button(
                    onClick = { permissionState.launchMultiplePermissionRequest() }
                ) {
                    Text(text = "Grant Permissions")
                }
            }
        }

        else -> {
            Column(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {
                Text(
                    text = "This app requires camera, audio, and notification permissions to function properly.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.Companion.padding(bottom = 16.dp)
                )
                Button(
                    onClick = { permissionState.launchMultiplePermissionRequest() }
                ) {
                    Text(text = "Request Permissions")
                }
            }
        }
    }
}
