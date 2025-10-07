package com.devansh.noteapp.di

import com.devansh.noteapp.core.database.DatabaseDriverFactory
import com.devansh.noteapp.data.repository.AppCacheSetting
import com.devansh.noteapp.data.repository.JvmSettingBuilder
import com.devansh.noteapp.data.repository.preference.AppCacheSettingImpl
import org.koin.core.module.Module
import org.koin.dsl.module
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory() }
    single { JvmSettingBuilder().createSettings() }
    single<AppCacheSetting> { AppCacheSettingImpl(get()) }
}

actual fun shareText(text: String, mimeType: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val selection = StringSelection(text)
    clipboard.setContents(selection, null)
}
