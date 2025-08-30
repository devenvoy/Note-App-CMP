package com.devansh.noteapp.di.platform_di

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry

actual fun clipEntryOf(string: String): ClipEntry = ClipEntry(ClipData("text", arrayOf("text/plain"), ClipData.Item(string)))

actual suspend fun ClipEntry.getText(): String? {
    return try {
        val itemCount = clipData.itemCount
        var textFull = ""
        for (i in 0 ..< itemCount) {
            val item = clipData.getItemAt(i)
            val text = item?.text
            if (text != null)
                textFull += text
        }
        textFull.ifEmpty { null }
    }catch (_: Exception){null}
}