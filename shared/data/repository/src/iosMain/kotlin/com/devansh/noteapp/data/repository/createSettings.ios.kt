package com.devansh.noteapp.data.repository

import com.russhwolf.settings.NSUserDefaultsSettings

actual class SettingBuilder {
    actual fun createSettings(): com.russhwolf.settings.Settings {
       return NSUserDefaultsSettings.Factory().create()
    }
}