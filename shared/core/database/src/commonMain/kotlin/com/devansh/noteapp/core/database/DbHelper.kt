package com.devansh.noteapp.core.database

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DbHelper(private val driverFactory: DatabaseDriverFactory) {

    private var db: NoteAppDatabase? = null

    private val mutex = Mutex()

    suspend fun <Result: Any> withDatabase(block: suspend (NoteAppDatabase) -> Result): Result  =  mutex.withLock {
        if (db == null) {
            db = createDb(driverFactory)
        }

        return@withLock block(db!!)
    }

    suspend fun <Result: Any> withDatabaseOrNull(block: suspend (NoteAppDatabase) -> Result?): Result?  =  mutex.withLock {
        if (db == null) {
            db = createDb(driverFactory)
        }

        return@withLock block(db!!)
    }

    private suspend fun createDb(driverFactory: DatabaseDriverFactory): NoteAppDatabase {
        return NoteAppDatabase(driver = driverFactory.createDriver())
    }
}