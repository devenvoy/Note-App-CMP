package com.devansh.noteapp.di.platform_di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual fun platformModule(): Module = module {
    single<SqlDriver> { JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY) }
}

actual fun shareText(text: String, mimeType: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val selection = StringSelection(text)
    clipboard.setContents(selection, null)
}
