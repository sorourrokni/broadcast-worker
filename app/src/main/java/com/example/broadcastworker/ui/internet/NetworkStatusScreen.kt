package com.example.broadcastworker.ui.internet

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun NetworkStatusScreen() {
    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(NetworkUtil.isNetworkAvailable(context)) }

    val networkChangeReceiver = remember {
        NetworkChangeReceiver({ connected ->
            isConnected = connected
        }, context)
    }

    DisposableEffect(Unit) {
        @Suppress("DEPRECATION")
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkChangeReceiver, intentFilter)
        onDispose {
            context.unregisterReceiver(networkChangeReceiver)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = if (isConnected) "Connected to the Internet" else "Disconnected from the Internet")
    }
}