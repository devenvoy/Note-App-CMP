package com.devansh.noteapp.di

import com.devansh.noteapp.core.database.DatabaseDriverFactory
import com.devansh.noteapp.data.repository.AppCacheSetting
import com.devansh.noteapp.data.repository.WebAppCacheSettingImpl
import com.devansh.noteapp.data.repository.WebSettingBuilder
import kotlinx.coroutines.DelicateCoroutinesApi
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(DelicateCoroutinesApi::class)
actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory() }
    single { WebSettingBuilder().createSettings() }
    single<AppCacheSetting> { WebAppCacheSettingImpl(get()) }
}

actual fun shareText(text: String, mimeType: String) {

}
