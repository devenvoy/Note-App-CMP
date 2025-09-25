package com.devansh.noteapp.data.repository

import com.russhwolf.settings.Settings

expect class SettingBuilder() {
    fun createSettings(): Settings
}