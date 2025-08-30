package com.devansh.noteapp.di.platform_di

import androidx.compose.ui.platform.ClipEntry

expect suspend fun ClipEntry.getText(): String?

expect fun clipEntryOf(string: String): ClipEntry
