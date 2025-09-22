package com.devansh.noteapp.core.utils

import androidx.compose.ui.platform.ClipEntry

expect suspend fun ClipEntry.getText(): String?

expect fun clipEntryOf(string: String): ClipEntry
