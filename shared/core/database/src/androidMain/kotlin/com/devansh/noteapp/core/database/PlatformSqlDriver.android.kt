package com.devansh.noteapp.core.database

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual fun getPlatformSqlDriver(context: Any?): SqlDriver {
    val ctx = context as? Context
        ?: throw IllegalArgumentException("Context required on Android")
    return AndroidSqliteDriver(NoteAppDatabase.Schema.synchronous(), ctx, "note_app.db")
}