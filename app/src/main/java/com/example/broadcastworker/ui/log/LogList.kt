package com.example.broadcastworker.ui.log

import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


@Composable
fun LogList(context: Context) {
    val logs = remember { readLogsFromFile(context) }

    LazyColumn {
        items(logs.reversed()) { log ->
            LogItem(log)
        }
    }
}

private fun readLogsFromFile(context: Context): List<LogEntry> {
    val LOG_FILE_NAME = "logs.json"
    val logFile = File(context.filesDir, LOG_FILE_NAME)
    if (!logFile.exists()) return emptyList()

    val jsonString = logFile.readText()
    val listType = object : TypeToken<List<LogEntry>>() {}.type
    return Gson().fromJson(jsonString, listType)
}