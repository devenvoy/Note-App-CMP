package com.devansh.noteapp.di.platform_di

import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.ExperimentalComposeUiApi

@OptIn(ExperimentalComposeUiApi::class)
actual suspend fun ClipEntry.getText(): String?= getPlainText()

@OptIn(ExperimentalComposeUiApi::class)
actual fun clipEntryOf(string: String) = ClipEntry.withPlainText(string)