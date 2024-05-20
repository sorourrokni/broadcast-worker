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

class BluetoothAirplaneWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        // Get BluetoothManager and BluetoothAdapter
        val bluetoothManager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val isBluetoothEnabled = bluetoothAdapter?.isEnabled == true

        // Check airplane mode status
        val isAirplaneModeOn = Settings.Global.getInt(
            applicationContext.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0

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