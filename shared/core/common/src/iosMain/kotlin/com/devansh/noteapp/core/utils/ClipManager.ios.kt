package com.devansh.noteapp.core.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

@OptIn(ExperimentalComposeUiApi::class)
actual suspend fun ClipEntry.getText(): String?= getPlainText()

@OptIn(ExperimentalComposeUiApi::class)
actual fun clipEntryOf(string: String) = ClipEntry.withPlainText(string)