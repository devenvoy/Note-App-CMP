package com.devansh.noteapp.core.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual fun getPlatformSqlDriver(context: Any?): SqlDriver {
    return NativeSqliteDriver(NoteAppDatabase.Schema.synchronous(), "note_db.db")
}