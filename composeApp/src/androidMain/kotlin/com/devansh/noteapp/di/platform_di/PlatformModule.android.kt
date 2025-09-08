package com.devansh.noteapp.di.platform_di

import android.content.Intent
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.devansh.noteapp.MainActivity
import com.devansh.noteapp.NoteApp
import com.devansh.noteapp.NoteAppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<SqlDriver> {
        val context = NoteApp.AppContext
        AndroidSqliteDriver(NoteAppDatabase.Schema.synchronous(), context, "app.db")
    }
}

actual fun shareText(text: String, mimeType: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = mimeType
        putExtra(Intent.EXTRA_TEXT, text)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    MainActivity.context.startActivity(Intent.createChooser(intent, "Share via"))
}