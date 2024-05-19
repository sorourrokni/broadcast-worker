package com.example.broadcastworker.ui.internet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkChangeReceiver(private val onNetworkChange: (Boolean) -> Unit, private val context: Context) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetworkUtil.isNetworkAvailable(context)
        onNetworkChange(isConnected)
        NotificationUtil.showNotification(context, isConnected)
    }
}