package com.example.broadcastworker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.broadcastworker.ui.airplaneBluetooth.BluetoothAirplaneWorker
import com.example.broadcastworker.ui.log.JsonFileObserver
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val logs = remember { mutableStateOf(readJsonFromFile(this)) }

                    val observer = remember {
                        JsonFileObserver(this, "logs.txt") {
                            logs.value = readJsonFromFile(this)
                        }
                    }

                    DisposableEffect(this) {
                        observer.startWatching()
                        onDispose {
                            observer.stopWatching()
                        }
                    }

                    LazyColumn {
                        items(logs.value) { log ->
                            Text(
                                text = log,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                            )
                            Divider()
                        }
                    }

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
    private fun readJsonFromFile(context: Context): List<String> {
        val logFile = File(context.filesDir, "logs.txt")
        val logs = mutableListOf<String>()
        if (logFile.exists()) {
            logFile.useLines { lines ->
                lines.forEach { line ->
                    val jsonObject = JSONObject(line)
                    val time = jsonObject.getString("time")

                    val isBluetoothEnabled = jsonObject.getBoolean("bluetooth_enabled")
                    val isAirplaneModeOn = jsonObject.getBoolean("airplane_mode")
                    val log =
                        "time: $time\n" +
                                "Bluetooth status is :${if (isBluetoothEnabled) "Enabled" else "Disabled"}\n" +
                                "Airplane mode is :" + " ${if (isAirplaneModeOn) "On" else "Off"}"

                    logs.add(log)
                }
            }
        }
        return logs.reversed()
    }
}