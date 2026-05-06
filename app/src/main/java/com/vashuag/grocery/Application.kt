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
