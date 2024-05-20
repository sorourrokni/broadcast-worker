package com.example.broadcastworker.ui.airplaneBluetooth

import android.bluetooth.BluetoothManager
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
class BluetoothAirplaneWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = dateFormat.format(Date())
        val jsonObject = JSONObject()

        // Get BluetoothManager and BluetoothAdapter
        val bluetoothManager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val isBluetoothEnabled = bluetoothAdapter?.isEnabled == true

        // Check airplane mode status
        val isAirplaneModeOn = Settings.Global.getInt(
            applicationContext.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0

        jsonObject.put("time", currentTime)
        jsonObject.put("bluetooth_enabled", isBluetoothEnabled)
        jsonObject.put("airplane_mode", isAirplaneModeOn)

        // Write JSON log to file
        val logFile = File(applicationContext.filesDir, "logs.json")
        logFile.appendText(jsonObject.toString() + "\n")

        // Log the status
        Log.i("worker_airplane", "Bluetooth is ${if (isBluetoothEnabled) "enabled" else "disabled"}")
        Log.i("worker_airplane", "Airplane mode is ${if (isAirplaneModeOn) "on" else "off"}")

        // Schedule the next work
        val nextWorkRequest = OneTimeWorkRequestBuilder<BluetoothAirplaneWorker>()
            .setInitialDelay(2, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(nextWorkRequest)

        return Result.success()
    }
}