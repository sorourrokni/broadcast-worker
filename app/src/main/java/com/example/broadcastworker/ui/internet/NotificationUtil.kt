package com.example.broadcastworker.ui.internet

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationUtil {
    private const val CHANNEL_ID = "network_status_channel"
    private const val CHANNEL_NAME = "Network Status"
    private const val NOTIFICATION_ID = 1

    fun showNotification(context: Context, isConnected: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Network Status")
            .setContentText(if (isConnected) "Connected to the Internet" else "Disconnected from the Internet")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
