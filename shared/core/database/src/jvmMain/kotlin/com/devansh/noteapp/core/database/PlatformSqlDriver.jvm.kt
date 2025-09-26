package com.devansh.noteapp.core.database

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File
import java.sql.DriverManager

actual class DatabaseDriverFactory {

    actual suspend fun createDriver(): SqlDriver {
        val dbFilePath: String = getPath(isDebug = false)
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${dbFilePath}")

        if (!File(dbFilePath).exists()) {
            NoteAppDatabase.Schema.awaitCreate(driver)
        }

        val currentVersion = getSQLiteSchemaVersion(dbFilePath)
        val latestVersion = NoteAppDatabase.Schema.version
        println("currentVersion: $currentVersion -> latestVersion:$latestVersion")
        if (currentVersion < latestVersion) {
            NoteAppDatabase.Schema.migrate(driver, currentVersion, latestVersion)
        }

        return driver
    }

    private fun getSQLiteSchemaVersion(dbPath: String): Long {
        val connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
        try {
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("PRAGMA user_version;")
            if (resultSet.next()) {
                return resultSet.getLong(1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.close()
        }

        return 0
    }

    private fun getPath(isDebug: Boolean): String {
        val propertyKey = if (isDebug) "java.io.tmpdir" else "user.home"
        val parentFolderPath = System.getProperty(propertyKey) + "/SqlDelightDemo"
        val parentFolder = File(parentFolderPath)
        if (!parentFolder.exists()) {
            parentFolder.mkdirs()
        }

        val databasePath = File(parentFolderPath, "note_app.db")
        return databasePath.absolutePath
    }
}