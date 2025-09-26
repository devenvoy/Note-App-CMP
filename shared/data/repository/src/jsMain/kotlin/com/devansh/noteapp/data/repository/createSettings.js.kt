package com.devansh.noteapp.data.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import kotlinx.browser.window

actual class SettingBuilder {
    actual fun createSettings(): Settings {
        val delegate = window.localStorage
        return StorageSettings(delegate)
    }
}