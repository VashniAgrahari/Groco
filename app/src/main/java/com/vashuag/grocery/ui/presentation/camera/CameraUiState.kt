package com.vashuag.grocery.ui.presentation.camera

import androidx.camera.view.PreviewView
import com.vashuag.grocery.ui.presentation.camera.analyzer.GraphicOverlay

sealed class CameraUiState {
    object Loading : CameraUiState()
    data class Ready(val previewView: PreviewView, val graphicOverlay: GraphicOverlay) : CameraUiState()
    data class Error(val message: String) : CameraUiState()
}