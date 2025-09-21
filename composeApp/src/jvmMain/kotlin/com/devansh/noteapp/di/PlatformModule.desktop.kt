package com.devansh.noteapp.di

import app.cash.sqldelight.db.SqlDriver
import com.devansh.noteapp.core.database.getPlatformSqlDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual fun platformModule(): Module = module {
    single<SqlDriver> {getPlatformSqlDriver()}
}

actual fun shareText(text: String, mimeType: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val selection = StringSelection(text)
    clipboard.setContents(selection, null)
}
