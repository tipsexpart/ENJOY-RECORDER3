package com.enjoy.recorder

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.enjoy.recorder.ads.AdManager

class EnjoyRecorderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            createNotificationChannels()
            Thread {
                try {
                    AdManager.init(this)
                } catch (e: Throwable) {
                    Log.e("EnjoyRecorderApp", "Background AdMob init: ${e.message}")
                }
            }.start()
        } catch (e: Throwable) {
            Log.e("EnjoyRecorderApp", "Application onCreate: ${e.message}", e)
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Screen Recording Service",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Persistent notification while screen recording is active"
                    setShowBadge(false)
                }
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager?.createNotificationChannel(channel)
            } catch (e: Exception) {
                Log.e("EnjoyRecorderApp", "Error creating channel: ${e.message}")
            }
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "enjoy_recorder_channel"
    }
}
