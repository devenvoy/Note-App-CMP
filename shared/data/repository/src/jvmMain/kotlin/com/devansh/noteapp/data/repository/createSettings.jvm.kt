package com.devansh.noteapp.data.repository

import com.russhwolf.settings.PreferencesSettings
import java.util.prefs.Preferences

class JvmSettingBuilder : SettingBuilder {
    override fun createSettings(): com.russhwolf.settings.Settings {
//        return PropertiesSettings(Properties())
        return PreferencesSettings(Preferences.userRoot())
    }
}