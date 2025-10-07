package com.devansh.noteapp.data.repository

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

class AndroidSettingBuilder(
    private val context: Context,
    private val name: String? = null
) : SettingBuilder {
    override fun createSettings(): Settings {
        return SharedPreferencesSettings.Factory(context).create(name ?: "my_prefs")
    }
}