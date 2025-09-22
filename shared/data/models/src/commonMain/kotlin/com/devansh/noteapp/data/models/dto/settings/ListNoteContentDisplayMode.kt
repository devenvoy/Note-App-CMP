package com.devansh.noteapp.data.models.dto.settings;

enum class ListNoteContentDisplayMode(private val value: Int) {
    RAW(0),
    PREVIEW(1);

    companion object {
        fun fromInt(value: Int) = entries.firstOrNull { it.value == value } ?: RAW
        fun ListNoteContentDisplayMode.toInt() = value
    }
}