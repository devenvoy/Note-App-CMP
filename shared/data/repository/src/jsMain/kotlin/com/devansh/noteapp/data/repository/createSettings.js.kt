package com.devansh.noteapp.data.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import kotlinx.browser.window

class WebSettingBuilder : SettingBuilder {
    override fun createSettings(): Settings {
        val delegate = window.localStorage
        return StorageSettings(delegate)
    }
}