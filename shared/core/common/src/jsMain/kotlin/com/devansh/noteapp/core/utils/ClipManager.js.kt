package com.devansh.noteapp.core.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import kotlinx.coroutines.await
import org.w3c.files.Blob
import org.w3c.files.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalComposeUiApi::class)
actual fun clipEntryOf(string: String) = ClipEntry.withPlainText(string)

@OptIn(ExperimentalComposeUiApi::class, ExperimentalWasmJsInterop::class)
actual suspend fun ClipEntry.getText(): String? {
    return try {
        val item = clipboardItems[0] ?: return null

        val hasTextPlain = item.types.toList().map { it }.any { it == "text/plain" }
        if (!hasTextPlain) return null
        val blob = item.getType("text/plain".toJsString()).await()
        blob.readAsText()
    } catch (_: Exception) {
        null
    }
}
suspend fun Blob.readAsText() = suspendCoroutine { cont ->
    val reader = FileReader()
    reader.onload = {
        cont.resume(reader.result.toString())
    }
    reader.onerror = {
        cont.resume(null)
    }
    reader.readAsText(this)
}