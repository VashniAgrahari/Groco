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
