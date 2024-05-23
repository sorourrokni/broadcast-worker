package com.example.broadcastworker.ui.log

import android.content.Context
import android.os.FileObserver
import java.io.File


class JsonFileObserver(
    private val context: Context,
    private val fileName: String,
    private val onFileChanged: () -> Unit

) : FileObserver(File(context.filesDir, fileName).absolutePath, CLOSE_WRITE) {

    override fun onEvent(event: Int, path: String?) {
        if (event == CLOSE_WRITE) {
            onFileChanged()
        }
    }
}