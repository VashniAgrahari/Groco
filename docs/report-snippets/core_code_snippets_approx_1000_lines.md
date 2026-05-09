# Groco Core Code Snippets (Approx. 1000 Lines)

This appendix collects core implementation snippets across app shell, storage, inventory, AI scanning, location config, notifications, and price comparison.

## App Entry: MainActivity
Path: `app/src/main/java/com/vashuag/grocery/MainActivity.kt:1-20`

```kotlin
package com.vashuag.grocery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vashuag.grocery.ui.presentation.App
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

```

## App Bootstrap: Application + Reminder Scheduler Hook
Path: `app/src/main/java/com/vashuag/grocery/Application.kt:1-13`

```kotlin
package com.vashuag.grocery

import android.app.Application
import com.vashuag.grocery.feature.inventory.notifications.ExpiryReminderScheduler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        ExpiryReminderScheduler.schedule(this)
    }
}
```

## ObjectBox Singleton Store
Path: `app/src/main/java/com/vashuag/grocery/ObjectBox.kt:1-23`

```kotlin
package com.vashuag.grocery

import android.content.Context
import com.vashuag.grocery.data.entity.MyObjectBox
import io.objectbox.BoxStore

class ObjectBox private constructor(val boxStore: BoxStore) {

    companion object {
        @Volatile
        private var instance: ObjectBox? = null

        fun init(context: Context): ObjectBox {
            return instance ?: synchronized(this) {
                instance ?: ObjectBox(
                    MyObjectBox.builder().androidContext(context.applicationContext).build()
                ).also { instance = it }
            }
        }

        fun get(): ObjectBox = instance ?: throw IllegalStateException("ObjectBox not initialized")
    }
}
```

## DI Module for ObjectBox Boxes
Path: `app/src/main/java/com/vashuag/grocery/ObjectBoxModule.kt:1-44`

```kotlin
package com.vashuag.grocery

import android.content.Context
import com.vashuag.grocery.data.entity.ComparisonHistory
import com.vashuag.grocery.data.entity.GroceryItem
import com.vashuag.grocery.data.entity.LocationSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ObjectBoxModule {

    @Provides
    @Singleton
    fun provideBoxStore(@ApplicationContext context: Context): BoxStore {
        return ObjectBox.init(context).boxStore
    }

    @Provides
    @Singleton
    fun provideGroceryBox(store: BoxStore): Box<GroceryItem> {
        return store.boxFor(GroceryItem::class.java)
    }

    @Provides
    @Singleton
    fun provideComparisonHistoryBox(store: BoxStore): Box<ComparisonHistory> {
        return store.boxFor(ComparisonHistory::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationSettingsBox(store: BoxStore): Box<LocationSettings> {
        return store.boxFor(LocationSettings::class.java)
    }

}
```

## Entity: GroceryItem (Embedding + Inventory Fields)
Path: `app/src/main/java/com/vashuag/grocery/data/entity/GroceryItem.kt:1-18`

```kotlin
package com.vashuag.grocery.data.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.HnswIndex
import io.objectbox.annotation.Id

@Entity
data class GroceryItem(
    @Id var id: Long = 0,
    var title: String = "",
    @HnswIndex(dimensions = 1280) var embeddings: FloatArray? = null,
    var imagePath: String = "",
    var expiryDateMs: Long = System.currentTimeMillis(),
    var quantity: Double = 1.0,
    var unit: String = "unit",
    var lowStockThreshold: Double = 1.0,
    var remindBeforeDays: Int = 2
)
```

## Entity: ComparisonHistory (Cache Persistence)
Path: `app/src/main/java/com/vashuag/grocery/data/entity/ComparisonHistory.kt:1-14`

```kotlin
package com.vashuag.grocery.data.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class ComparisonHistory(
    @Id var id: Long = 0,
    var fingerprint: String = "",
    var queryText: String = "",
    var locationJson: String = "",
    var responseJson: String = "",
    var createdAtMs: Long = System.currentTimeMillis()
)
```

## Entity: LocationSettings (Saved Config)
Path: `app/src/main/java/com/vashuag/grocery/data/entity/LocationSettings.kt:1-16`

```kotlin
package com.vashuag.grocery.data.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class LocationSettings(
    @Id var id: Long = 0L,
    var city: String = "",
    var area: String = "",
    var pincode: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var hasCoordinates: Boolean = false,
    var maxResultsPerSite: Int = 8
)
```

## HomeViewModel: Inventory CRUD and Quantity Controls
Path: `app/src/main/java/com/vashuag/grocery/ui/presentation/home/HomeViewModel.kt:1-111`

```kotlin
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
```

## HomeScreen (Core List + Actions)
Path: `app/src/main/java/com/vashuag/grocery/ui/presentation/home/HomeScreen.kt:57-120`

```kotlin
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
```

## Location Settings Repository
Path: `app/src/main/java/com/vashuag/grocery/feature/settings/data/LocationSettingsRepository.kt:1-77`

```kotlin
package com.vashuag.grocery.feature.settings.data

import com.vashuag.grocery.data.entity.LocationSettings
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

data class SavedLocationSettings(
    val city: String = "",
    val area: String = "",
    val pincode: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val maxResultsPerSite: Int = 8
)

@Singleton
class LocationSettingsRepository @Inject constructor(
    private val settingsBox: Box<LocationSettings>
) {

    fun getSavedSettings(): SavedLocationSettings {
        val entity = getOrCreateEntity()
        return SavedLocationSettings(
            city = entity.city.trim(),
            area = entity.area.trim(),
            pincode = entity.pincode.trim(),
            latitude = if (entity.hasCoordinates) entity.latitude else null,
            longitude = if (entity.hasCoordinates) entity.longitude else null,
            maxResultsPerSite = entity.maxResultsPerSite.coerceIn(1, 30)
        )
    }

    fun saveSettings(settings: SavedLocationSettings) {
        val entity = getOrCreateEntity()
        entity.city = settings.city.trim()
        entity.area = settings.area.trim()
        entity.pincode = settings.pincode.filter { it.isDigit() }.take(6)
        entity.latitude = settings.latitude ?: 0.0
        entity.longitude = settings.longitude ?: 0.0
        entity.hasCoordinates = settings.latitude != null && settings.longitude != null
        entity.maxResultsPerSite = settings.maxResultsPerSite.coerceIn(1, 30)
        settingsBox.put(entity)
    }

    fun resetDefaults() {
        val defaults = defaultEntity()
        val entity = getOrCreateEntity()
        entity.city = defaults.city
        entity.area = defaults.area
        entity.pincode = defaults.pincode
        entity.latitude = defaults.latitude
        entity.longitude = defaults.longitude
        entity.hasCoordinates = defaults.hasCoordinates
        entity.maxResultsPerSite = defaults.maxResultsPerSite
        settingsBox.put(entity)
    }

    private fun defaultEntity(): LocationSettings {
        return LocationSettings(
            city = "Bengaluru",
            area = "Indiranagar",
            pincode = "560038",
            latitude = 12.9716,
            longitude = 77.5946,
            hasCoordinates = true,
            maxResultsPerSite = 8
        )
    }

    private fun getOrCreateEntity(): LocationSettings {
        settingsBox.all.firstOrNull()?.let { return it }
        val entity = defaultEntity()
        settingsBox.put(entity)
        return entity
    }
}
```

## Expiry Reminder Scheduler (WorkManager)
Path: `app/src/main/java/com/vashuag/grocery/feature/inventory/notifications/ExpiryReminderScheduler.kt:1-47`

```kotlin
package com.vashuag.grocery.feature.inventory.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object ExpiryReminderScheduler {

    const val WORK_NAME = "expiry-reminder-work"
    const val CHANNEL_ID = "expiry-reminders"

    fun schedule(context: Context) {
        ensureNotificationChannel(context)

        val workRequest = PeriodicWorkRequestBuilder<ExpiryReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    private fun ensureNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Expiry reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for nearby grocery expiry dates"
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
```

## Expiry Reminder Worker (Notification Trigger Logic)
Path: `app/src/main/java/com/vashuag/grocery/feature/inventory/notifications/ExpiryReminderWorker.kt:1-67`

```kotlin
package com.vashuag.grocery.feature.inventory.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vashuag.grocery.ObjectBox
import com.vashuag.grocery.R
import com.vashuag.grocery.data.entity.GroceryItem
import io.objectbox.Box
import java.util.concurrent.TimeUnit

class ExpiryReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return runCatching {
            val groceryBox: Box<GroceryItem> = ObjectBox.get().boxStore.boxFor(GroceryItem::class.java)
            val items = groceryBox.all
            val now = System.currentTimeMillis()
            items.forEach { item ->
                notifyIfDue(item, now)
            }
            Result.success()
        }.getOrElse {
            Result.retry()
        }
    }

    private fun notifyIfDue(item: GroceryItem, nowMs: Long) {
        val daysRemaining = TimeUnit.MILLISECONDS.toDays(item.expiryDateMs - nowMs)
        val shouldNotify = daysRemaining <= item.remindBeforeDays.toLong()
        if (!shouldNotify) {
            return
        }

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val body = if (daysRemaining < 0) {
            "${item.title} expired ${kotlin.math.abs(daysRemaining)} day(s) ago."
        } else {
            "${item.title} expires in $daysRemaining day(s)."
        }

        val notification = NotificationCompat.Builder(applicationContext, ExpiryReminderScheduler.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Expiry reminder")
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(item.id.toInt(), notification)
    }
}
```

## Scanner Analyzer: ML Kit Detection + Crop Pipeline
Path: `app/src/main/java/com/vashuag/grocery/ui/presentation/camera/MainViewModel.kt:130-215`

```kotlin
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
```

## Scanner Post-Processing: Embedding Similarity + Gemini Label/Expiry + Store
Path: `app/src/main/java/com/vashuag/grocery/ui/presentation/camera/MainViewModel.kt:325-443`

```kotlin
    fun getEmbeddingSimilarity(index: Int): List<ObjectWithScore<GroceryItem>> {
        val groceryItem = scanningState.detectedObjects[index]
        if (groceryItem.imageEmbeddings.isEmpty()) {
            return emptyList()
        }
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
                expiryDateMs = expiryDateMs,
                quantity = 1.0,
                unit = "unit",
                lowStockThreshold = 1.0,
                remindBeforeDays = 2
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
            if (size < 2) {
                return@launch
            }
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
```

## Compare Domain Models
Path: `app/src/main/java/com/vashuag/grocery/feature/compare/domain/CompareModels.kt:1-60`

```kotlin
package com.vashuag.grocery.feature.compare.domain

import kotlinx.serialization.Serializable

@Serializable
data class UserLocation(
    val city: String? = null,
    val area: String? = null,
    val pincode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)

@Serializable
data class CompareRequest(
    val location: UserLocation = UserLocation(),
    val itemName: String? = null,
    val searchTag: String? = null,
    val maxResultsPerSite: Int = 8
) {
    val queryText: String
        get() = (itemName ?: searchTag ?: "").trim()

    fun isValid(): Boolean {
        return queryText.isNotBlank() && maxResultsPerSite in 1..30
    }
}

@Serializable
data class Offer(
    val site: String,
    val title: String,
    val price: Double,
    val currency: String = "INR",
    val brand: String? = null,
    val sizeText: String? = null,
    val inStock: Boolean? = null,
    val url: String? = null,
    val imageUrl: String? = null,
    val score: Double? = null,
    val raw: Map<String, String> = emptyMap()
)

@Serializable
data class MatchedItem(
    val canonicalName: String,
    val confidence: Double,
    val offers: List<Offer>
)

@Serializable
data class CompareResult(
    val query: String,
    val location: UserLocation,
    val matchedItems: List<MatchedItem>,
    val generatedAtMs: Long,
    val fromCache: Boolean = false,
    val rawSiteResults: Map<String, List<Offer>> = emptyMap(),
    val siteErrors: Map<String, String?> = emptyMap()
)
```

## Compare Networking: Ktor Client Module
Path: `app/src/main/java/com/vashuag/grocery/feature/compare/data/NetworkModule.kt:1-36`

```kotlin
package com.vashuag.grocery.feature.compare.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(HttpTimeout) {
                requestTimeoutMillis = 8_000
                connectTimeoutMillis = 8_000
                socketTimeoutMillis = 12_000
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }
}
```

## Compare Repository: Multi-Site Fanout, Filtering, and Cache
Path: `app/src/main/java/com/vashuag/grocery/feature/compare/data/PriceComparisonRepository.kt:35-198`

```kotlin
    suspend fun compare(request: CompareRequest): CompareResult {
        val now = System.currentTimeMillis()
        if (!request.isValid()) {
            return CompareResult(
                query = request.queryText,
                location = request.location,
                matchedItems = emptyList(),
                generatedAtMs = now,
                fromCache = false,
                rawSiteResults = emptyMap(),
                siteErrors = mapOf("request" to "Invalid request")
            )
        }

        val fingerprint = fingerprint(request.queryText, request.location)
        val cached = getRecentCache(fingerprint)
        if (cached != null) {
            val cachedResult = runCatching {
                json.decodeFromString<CompareResult>(cached.responseJson)
            }.getOrNull()
            if (cachedResult != null) {
                return cachedResult.copy(fromCache = true)
            }
        }

        val siteResults = fetchAllSites(request)
        val rawSiteResults = siteResults.associate { it.site to it.offers }
        val siteErrors = siteResults.associate { it.site to it.error }
        val flatOffers = siteResults.flatMap { it.offers }

        val matched = matcher.match(request.queryText, flatOffers)

        val response = CompareResult(
            query = request.queryText,
            location = request.location,
            matchedItems = matched,
            generatedAtMs = now,
            fromCache = false,
            rawSiteResults = rawSiteResults,
            siteErrors = siteErrors
        )

        storeCache(
            fingerprint = fingerprint,
            queryText = request.queryText,
            location = request.location,
            result = response
        )

        return response
    }

    private suspend fun fetchAllSites(request: CompareRequest): List<SiteResult> = coroutineScope {
        adapters.map { adapter ->
            async(Dispatchers.IO) {
                runCatching {
                    val offers = adapter.search(
                        query = request.queryText,
                        location = request.location,
                        maxResults = request.maxResultsPerSite
                    )
                    val filtered = filterRelevantOffers(request.queryText, offers)
                    if (filtered.isEmpty()) {
                        SiteResult(
                            site = adapter.name,
                            offers = emptyList(),
                            error = "No parsable offers from site or blocked by anti-bot/location gate"
                        )
                    } else {
                        SiteResult(site = adapter.name, offers = filtered.take(request.maxResultsPerSite), error = null)
                    }
                }.getOrElse { throwable ->
                    SiteResult(
                        site = adapter.name,
                        offers = emptyList(),
                        error = throwable.message?.ifBlank { null }
                            ?: "${throwable::class.simpleName} while fetching site data"
                    )
                }
            }
        }.awaitAll()
    }

    private fun filterRelevantOffers(query: String, offers: List<Offer>): List<Offer> {
        val queryNorm = normalizeText(query)
        if (queryNorm.isBlank()) {
            return offers
        }

        val ranked = offers.mapNotNull { offer ->
            var score = matcher.offerQueryScore(query, offer)
            val titleNorm = normalizeText(offer.title)
            if (queryNorm in titleNorm) {
                score += 12.0
            }
            if (score >= 44.0) {
                score to offer
            } else {
                null
            }
        }.sortedByDescending { it.first }

        return if (ranked.isNotEmpty()) {
            ranked.map { it.second }
        } else {
            offers
        }
    }

    private fun getRecentCache(fingerprint: String): ComparisonHistory? {
        val ttlMs = 30L * 60L * 1000L
        val cutoff = System.currentTimeMillis() - ttlMs

        val query = historyBox.query(ComparisonHistory_.fingerprint.equal(fingerprint)).build()
        return try {
            query.find()
                .asSequence()
                .filter { history -> history.createdAtMs >= cutoff }
                .maxByOrNull { history -> history.createdAtMs }
        } finally {
            query.close()
        }
    }

    private fun storeCache(
        fingerprint: String,
        queryText: String,
        location: UserLocation,
        result: CompareResult
    ) {
        val locationJson = json.encodeToString(location)
        val responseJson = json.encodeToString(result)
        historyBox.put(
            ComparisonHistory(
                fingerprint = fingerprint,
                queryText = queryText,
                locationJson = locationJson,
                responseJson = responseJson,
                createdAtMs = System.currentTimeMillis()
            )
        )
    }

    private fun fingerprint(query: String, location: UserLocation): String {
        val locationMap = listOfNotNull(
            location.city?.let { "city=${it.lowercase()}" },
            location.area?.let { "area=${it.lowercase()}" },
            location.pincode?.filter { it.isDigit() }?.let { "pincode=$it" },
            location.latitude?.let { "lat=$it" },
            location.longitude?.let { "lng=$it" }
        ).sorted().joinToString("|")

        val payload = "${normalizeText(query)}|$locationMap"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(payload.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }

    private data class SiteResult(
        val site: String,
        val offers: List<Offer>,
        val error: String?
    )
}
```

## CompareScreen (Query + Location Summary + Trigger UI)
Path: `app/src/main/java/com/vashuag/grocery/feature/compare/ui/CompareScreen.kt:47-125`

```kotlin
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
```

