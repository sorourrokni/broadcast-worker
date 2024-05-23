package com.example.broadcastworker.ui.airplaneBluetooth

import android.bluetooth.BluetoothManager
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class BluetoothAirplaneWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {
    override fun doWork(): Result {
        // Get BluetoothManager and BluetoothAdapter
        val bluetoothManager =
            applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val isBluetoothEnabled = bluetoothAdapter?.isEnabled == true

        // Check airplane mode status
        val isAirplaneModeOn = Settings.Global.getInt(
            applicationContext.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0

        // Log the status
        Log.i(
            "worker_airplane",
            "Bluetooth is ${if (isBluetoothEnabled) "enabled" else "disabled"}"
        )
        Log.i("worker_airplane", "Airplane mode is ${if (isAirplaneModeOn) "on" else "off"}")

        // Write JSON log to file

        writeLogsToFile(
            isBluetoothEnabled = isBluetoothEnabled,
            isAirplaneModeOn = isAirplaneModeOn
        )

        // Schedule the next work
        val nextWorkRequest = OneTimeWorkRequestBuilder<BluetoothAirplaneWorker>()
            .setInitialDelay(2, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(nextWorkRequest)

        return Result.success()
    }

    private fun writeLogsToFile(isBluetoothEnabled: Boolean, isAirplaneModeOn: Boolean) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = dateFormat.format(Date())
        val jsonObject = JSONObject()
        val logFile = File(applicationContext.filesDir, "logs.txt")

        jsonObject.put("time", currentTime)
        jsonObject.put("bluetooth_enabled", isBluetoothEnabled)
        jsonObject.put("airplane_mode", isAirplaneModeOn)

        val jsonString = jsonObject.toString() + "\n"
        logFile.appendText(jsonString)
    }
    fun readLogsFromFile(): List<JSONObject> {
        val logFile = File(applicationContext.filesDir, "logs.txt")
        if (!logFile.exists()) {
            return emptyList()
        }

        val logs = mutableListOf<JSONObject>()
        logFile.forEachLine { line ->
            val jsonObject = JSONObject(line)
            logs.add(jsonObject)
        }
        return logs
    }
}