package com.devansh.noteapp.data.local

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.devansh.noteapp.NoteDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(schema = NoteDatabase.Schema.synchronous(), context =  context, name =  "note_db.db")
    }
}