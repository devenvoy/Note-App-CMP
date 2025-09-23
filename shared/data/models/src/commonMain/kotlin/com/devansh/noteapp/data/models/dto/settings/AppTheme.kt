package com.devansh.noteapp.data.models.dto.settings

enum class AppTheme(private val value: Int) {
    UNDEFINED(-1),
    SYSTEM(0),
    LIGHT(1),
    DARK(2);

    companion object {
        fun fromInt(value: Int) = entries.firstOrNull { it.value == value } ?: UNDEFINED
        fun AppTheme.toInt() = value
    }
}