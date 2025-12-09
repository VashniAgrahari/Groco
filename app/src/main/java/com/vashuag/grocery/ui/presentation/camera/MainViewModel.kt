package com.vashuag.grocery.ui.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder.ImageEmbedderOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.vashuag.grocery.data.entity.GroceryItem
import com.vashuag.grocery.data.entity.GroceryItem_
import com.vashuag.grocery.ui.presentation.camera.analyzer.GraphicOverlay
import com.vashuag.grocery.ui.presentation.camera.analyzer.ObjectGraphic
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.objectbox.Box
import io.objectbox.query.ObjectWithScore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context, private val groceryBox: Box<GroceryItem>
) : ViewModel() {
    companion object {

    private const val TAG = "MainViewModel"
    }
    private val _uiState = MutableStateFlow<CameraUiState>(CameraUiState.Loading)
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    private var cameraProvider: ProcessCameraProvider? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var preview: Preview? = null
    private var previewView: PreviewView? = null
    private var graphicOverlay: GraphicOverlay? = null

    private var needUpdateGraphicOverlayImageSourceInfo = false

    var shouldStoreNextFrame = false

    var scanningState by mutableStateOf(ScanningState())
        private set


    fun initializeCamera(lifecycleOwner: LifecycleOwner) {
        if (_uiState.value is CameraUiState.Ready) return

        viewModelScope.launch {
            try {
                val provider = ProcessCameraProvider.getInstance(context).get()
                cameraProvider = provider

                setupCameraUseCases(lifecycleOwner)

            } catch (exception: Exception) {
                _uiState.value = CameraUiState.Error(
                    exception.message ?: "Unknown camera error"
                )
            }
        }
    }

    private fun setupCameraUseCases(lifecycleOwner: LifecycleOwner) {
        val provider = cameraProvider ?: return

        try {
            val previewView = PreviewView(context)
            this.previewView = previewView

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }
            this.preview = preview
            val graphicOverlay = GraphicOverlay(
                context, null
            )
            this.graphicOverlay = graphicOverlay

            val imageAnalyzer = createImageAnalysisUseCase()
            this.imageAnalyzer = imageAnalyzer

            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            provider.unbindAll()
            provider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, imageAnalyzer
            )
            _uiState.value = CameraUiState.Ready(previewView, graphicOverlay)
        } catch (exception: Exception) {
            _uiState.value = CameraUiState.Error(
                "Failed to bind camera use cases: ${exception.message}"
            )
        }
    }


    class ObjectDetectionAnalyzer(
        private val onObjectsDetected: (List<DetectedObjectImage>) -> Unit
    ) {
        private val options =
            ObjectDetectorOptions.Builder().setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                .enableClassification().build()
        private val detector: ObjectDetector = ObjectDetection.getClient(options)

        @OptIn(ExperimentalGetImage::class)
        fun processImageProxy(
            imageProxy: ImageProxy,
            overlay: GraphicOverlay,
            shouldStoreNextFrame: Boolean,
            step: ScanStep
        ) {
            val mediaImage = imageProxy.image!!
            val inputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val task = detector.process(inputImage)
            task.addOnSuccessListener { results ->
                overlay.clear()
                for (result in results) {
                    overlay.add(ObjectGraphic(overlay, result))
                }
                if (shouldStoreNextFrame && results.isNotEmpty()) {

                    // Convert ImageProxy to Bitmap
                    val bitmap = imageProxy.toBitmap()

                    // Crop images for each detected object
                    val croppedImages = results.mapNotNull { detectedObject ->
                        try {
                            val boundingBox = detectedObject.boundingBox

                            // Ensure bounding box is within image bounds
                            val left = boundingBox.left.coerceIn(0, bitmap.width)
                            val top = boundingBox.top.coerceIn(0, bitmap.height)
                            val right = boundingBox.right.coerceIn(0, bitmap.width)
                            val bottom = boundingBox.bottom.coerceIn(0, bitmap.height)

                            val width = (right - left).coerceAtLeast(1)
                            val height = (bottom - top).coerceAtLeast(1)

                            val matrix = Matrix().apply {
                                postRotate(90f)   // clockwise 90 degrees
                            }

                            val rotatedBitmap = Bitmap.createBitmap(
                                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                            )

                            val croppedBitmap = Bitmap.createBitmap(
                                rotatedBitmap, left, top, width, height
                            )

                            // Extract labels and confidence scores
                            val labels = detectedObject.labels.map { it.text }
                            val confidences = detectedObject.labels.map { it.confidence }

                            DetectedObjectImage(
                                bitmap = croppedBitmap,
                                boundingBox = Rect(left, top, right, bottom),
                                labels = labels,
                                confidence = confidences,
                                trackingId = detectedObject.trackingId,
                                imageEmbeddings = listOf(),
                                step = step
                            )
                        } catch (e: Exception) {
                            Log.e("ObjectDetectionAnalyzer", "Error cropping image", e)
                            null
                        }
                    }

                    if (croppedImages.isNotEmpty()) {
                        onObjectsDetected(croppedImages)
                    }
                }
                overlay.postInvalidate()
                imageProxy.close()
            }.addOnFailureListener { exception ->
                Log.e("ObjectDetectionAnalyzer", "Object detection failed", exception)
                imageProxy.close()
            }
        }
    }

    private fun createImageAnalysisUseCase(): ImageAnalysis {
        val options = ImageEmbedderOptions.builder().setBaseOptions(
            BaseOptions.builder().setModelAssetPath("mobilenet_v3_large.tflite").build()
        ).setRunningMode(RunningMode.IMAGE).build()
        val imageEmbedder = ImageEmbedder.createFromOptions(context, options)

        val executor = ContextCompat.getMainExecutor(context)
        val analyzer = ObjectDetectionAnalyzer { detectedObjectImages ->
            val detectedObjectsWithEmbeddings = detectedObjectImages.map { detectedObjectImage ->
                val mpImage = BitmapImageBuilder(detectedObjectImage.bitmap).build()
                val embedderResult = imageEmbedder.embed(mpImage)
                val embeddings = embedderResult.embeddingResult().embeddings()
                val embeddingList = if (embeddings.isNotEmpty()) {
                    embeddings[0].floatEmbedding().toList()
                } else {
                    emptyList()
                }
                Log.d("ObjectDetectionAnalyzer", "createImageAnalysisUseCase: $embeddingList")
                Log.d(
                    "ObjectDetectionAnalyzer", "Generated embeddings of size: ${embeddingList.size}"
                )
                detectedObjectImage.copy(imageEmbeddings = embeddingList)
            }
            scanningState = scanningState.copy(
                detectedObjects = scanningState.detectedObjects + detectedObjectsWithEmbeddings
            )
            moveToNextScanStep()
        }
        needUpdateGraphicOverlayImageSourceInfo = true
        return ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
            .also { analysis ->
                analysis.setAnalyzer(executor) { imageProxy ->
                    if (needUpdateGraphicOverlayImageSourceInfo) {
                        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                        if (rotationDegrees == 0 || rotationDegrees == 180) {
                            graphicOverlay!!.setImageSourceInfo(
                                imageProxy.width, imageProxy.height, false
                            )
                        } else {
                            graphicOverlay!!.setImageSourceInfo(
                                imageProxy.height, imageProxy.width, false
                            )
                        }
                        needUpdateGraphicOverlayImageSourceInfo = false
                    }
                    analyzer.processImageProxy(
                        imageProxy,
                        graphicOverlay!!,
                        shouldStoreNextFrame,
                        scanningState.currentStep
                    )
                    shouldStoreNextFrame = false
                }
            }
    }

    fun captureCurrentImage() {
        shouldStoreNextFrame = true
    }

    fun moveToNextScanStep() {
        if (scanningState.currentStep == ScanStep.BARCODE_IMAGE) {
            detectAndAddLastGroceryItem()
        }
        scanningState = when (scanningState.currentStep) {
            ScanStep.FRONT_IMAGE -> scanningState.copy(currentStep = ScanStep.BARCODE_IMAGE)
            ScanStep.BARCODE_IMAGE -> scanningState.copy(currentStep = ScanStep.FRONT_IMAGE)
        }
    }

    override fun onCleared() {
        super.onCleared()
        imageAnalyzer?.clearAnalyzer()
        cameraProvider?.unbindAll()

        imageAnalyzer = null
        preview = null
        previewView = null
        cameraProvider = null
    }

    fun stopScanning() {
        scanningState = scanningState.copy(isScanning = false)
    }


    fun startScanning() {
        scanningState = scanningState.copy(isScanning = true)
    }

    fun getEmbeddingSimilarity(index: Int): List<ObjectWithScore<GroceryItem>> {
        val groceryItem = scanningState.detectedObjects[index]
        val query = groceryBox.query(
            GroceryItem_.embeddings.nearestNeighbors(
                groceryItem.imageEmbeddings.toFloatArray(), 5
            )
        ).build()
        val results = query.findWithScores()
        return results
    }

    fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap, filename: String): String {
        val file = File(context.filesDir, filename)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file.absolutePath
    }

    fun storeItem(index: Int, expiryDate: String, title: String) {
        var expiryDateMs = System.currentTimeMillis()

        // Parse expiry date if it's not null
        if (expiryDate != "null" && expiryDate.isNotBlank()) {
            try {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val parsedDate = dateFormat.parse(expiryDate)

                if (parsedDate != null) {
                    // Set time to midnight (12:00 AM) in local time
                    val calendar = Calendar.getInstance()
                    calendar.time = parsedDate
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)

                    expiryDateMs = calendar.timeInMillis
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error parsing expiry date: $expiryDate", e)
                // Keep default value (current time) if parsing fails
            }
        }

        val groceryItem = scanningState.detectedObjects[index]
        val path = saveBitmapToInternalStorage(
            context,
            groceryItem.bitmap,
            "grocery_item_${System.currentTimeMillis()}_${Math.random() * 100}.png"
        )
        groceryBox.put(
            GroceryItem(
                title = title,
                embeddings = groceryItem.imageEmbeddings.toFloatArray(),
                imagePath = path,
                expiryDateMs = expiryDateMs
            )
        )
    }

    suspend fun detectObjectInImage(index: Int): String {
        Log.d(TAG, "detectObjectInImage: Calling AI for detection")
        val model =
            Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel("gemini-2.5-flash")
        val response = model.generateContent(
            content {
                image(scanningState.detectedObjects[index].bitmap)
                text("Detect the name of the grocery item in this image, you need to only give the descriptive title of the detected object in only the max 5 words, like \"Lays Chips\", \"Basmati Rice\" etc.")
            })
        Log.d(TAG, "detectObjectInImage: ${response.text}")
        return response.text ?: ""
    }

    suspend fun getExpiryDateFromImage(index: Int): String {
        Log.d(TAG, "detectObjectInImage: Calling AI for expiry date")
        val model =
            Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel("gemini-2.5-flash")
        val response = model.generateContent(
            content {
                image(scanningState.detectedObjects[index].bitmap)
                text("Return the Expiry Date from the product detail in the image in DD/MM/YYYY format, you need to check what is the correct expiry date, the manufacturing date may also be present, you need to return the expiry date, and only the date in the specified format, like \"02/06/2026\" etc., In some cases the text expiry date or manufacturing date or use by might not be clearly visible, but the actual date is sure to be visible, and in case both dates are present, then the later date is the expiry date. look carefully for all the text in the image, the expiry date must be present on the page, it may be in any format, in MM/YYYY or in complete DD/MM/YYYY format, or any similar format, and it might not be clearly visible, but you need to identify it. As the last resort, return \"null\" only if you are unable to find any relevant expiry date. Final result you should only return in one word, either the date or the null string, NOTHING ELSE")
            })
        Log.d(TAG, "getExpiryDateFromImage: ${response.text}")
        return response.text ?: ""
    }

    fun detectAndAddLastGroceryItem() {
        viewModelScope.launch {
            val size = scanningState.detectedObjects.size
            val scores = getEmbeddingSimilarity(size-2)
            var foundItem: ObjectWithScore<GroceryItem>? = null
            for (score in scores) {
                Log.d(TAG, "detectAndAddLastGroceryItem: ${score.score}")
                val currentScore = score.score
                val foundScore = foundItem?.score ?: 1000.0
                if (currentScore <= 100 && foundScore > currentScore) {
                    foundItem = score
                }
            }
            var title = foundItem?.get()?.title
            Log.d(TAG, "detectAndAddLastGroceryItem: Found existing title : $title")
            if (title == null){
                title = detectObjectInImage(size-2)
            }
            val expiryDate = getExpiryDateFromImage(size-1)
            storeItem(size-2, expiryDate, title)
        }
    }
}

enum class ScanStep {
    FRONT_IMAGE, BARCODE_IMAGE
}

data class DetectedObjectImage(
    val bitmap: Bitmap,
    val boundingBox: Rect,
    val labels: List<String>,
    val confidence: List<Float>,
    val trackingId: Int?,
    val imageEmbeddings: List<Float>,
    val step: ScanStep
)

data class ScanningState(
    val isScanning: Boolean = true,
    val currentStep: ScanStep = ScanStep.FRONT_IMAGE,
    val detectedObjects: List<DetectedObjectImage> = emptyList()
)