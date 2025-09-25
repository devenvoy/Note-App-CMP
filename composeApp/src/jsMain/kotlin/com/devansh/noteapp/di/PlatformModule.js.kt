package com.devansh.noteapp.di

import app.cash.sqldelight.db.SqlDriver
import com.devansh.noteapp.core.database.DatabaseDriverFactory
import com.devansh.noteapp.data.repository.SettingBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<SqlDriver> {
        DatabaseDriverFactory().createDriver()
    }
    single {
        SettingBuilder().createSettings()
    }
}

actual fun shareText(text: String, mimeType: String) {

}
