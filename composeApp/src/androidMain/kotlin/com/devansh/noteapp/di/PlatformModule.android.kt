package com.devansh.noteapp.di

import android.content.Intent
import com.devansh.noteapp.MainActivity
import com.devansh.noteapp.NoteApp
import com.devansh.noteapp.core.database.DatabaseDriverFactory
import com.devansh.noteapp.data.repository.SettingBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory(NoteApp.AppContext) }
    single { SettingBuilder(NoteApp.AppContext).createSettings() }
}

actual fun shareText(text: String, mimeType: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = mimeType
        putExtra(Intent.EXTRA_TEXT, text)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    MainActivity.Companion.context.startActivity(Intent.createChooser(intent, "Share via"))
}