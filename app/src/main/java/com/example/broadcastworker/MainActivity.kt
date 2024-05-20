package com.example.broadcastworker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.work.*
import com.example.broadcastworker.ui.airplaneBluetooth.BluetoothAirplaneWorker
import com.example.broadcastworker.ui.internet.NetworkStatusScreen
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    NetworkStatusScreen()
                }
            }
        }
        startOneTimeWork()
    }

    private fun startOneTimeWork() {
        val workRequest = OneTimeWorkRequestBuilder<BluetoothAirplaneWorker>()
            .setInitialDelay(2, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "BluetoothAirplaneWork",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
