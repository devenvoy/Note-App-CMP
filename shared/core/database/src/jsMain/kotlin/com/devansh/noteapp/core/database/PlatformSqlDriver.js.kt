package com.devansh.noteapp.core.database

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.w3c.dom.Worker

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
       /* return GlobalScope.promise{
            val driver = WebWorkerDriver(
                Worker(
                    js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
                )
            ).also {
                NoteAppDatabase.Schema.create(it).await()
            }
          driver
        }.unsafeCast<SqlDriver>()*/

        val driver = WebWorkerDriver(
            Worker(
                js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
            )
        )
        driver.execute(null, "PRAGMA foreign_keys = ON;", 0)
        NoteAppDatabase.Schema.awaitCreate(driver)
        return driver
    }
}