package com.devansh.noteapp.data.repository

import com.russhwolf.settings.Settings

interface SettingBuilder {
    fun createSettings(): Settings
}