package com.example.broadcastworker.ui.log

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LogItem(log: LogEntry) {
    Column {
        Text(text = "Time: ${log.time}")
        Text(text = "Bluetooth Enabled: ${if (log.bluetoothEnabled) "Enabled" else "Disabled"}")
        Text(text = "Airplane Mode: ${if (log.airplaneMode) "On" else "Off"}")
    }
}
