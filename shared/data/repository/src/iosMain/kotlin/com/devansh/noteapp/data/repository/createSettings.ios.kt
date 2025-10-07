package com.devansh.noteapp.data.repository

import com.russhwolf.settings.NSUserDefaultsSettings

class IosSettingBuilder : SettingBuilder {
    override fun createSettings(): com.russhwolf.settings.Settings {
       return NSUserDefaultsSettings.Factory().create()
    }
}