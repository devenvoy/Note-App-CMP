package com.devansh.noteapp.data.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

actual class SettingBuilder {
    actual fun createSettings(): Settings = StorageSettings()
}