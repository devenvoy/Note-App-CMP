package com.devansh.noteapp.di.platform_di

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.devansh.noteapp.MainActivity
import com.devansh.noteapp.NoteApp
import com.devansh.noteapp.NoteDatabase
import com.devansh.noteapp.data.local.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { NoteDatabase(DatabaseDriverFactory(NoteApp.AppContext).createDriver()) }
}

actual fun shareText(text: String, mimeType: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = mimeType
        putExtra(Intent.EXTRA_TEXT, text)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ensure this is added
    }
    MainActivity.context.startActivity(Intent.createChooser(intent, "Share via"))
}


actual fun isDesktop(): Boolean = false