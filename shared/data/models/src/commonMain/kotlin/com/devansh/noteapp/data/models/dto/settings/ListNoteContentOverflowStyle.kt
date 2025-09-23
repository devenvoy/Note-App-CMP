package com.devansh.noteapp.data.models.dto.settings;

enum class ListNoteContentOverflowStyle(private val value: Int) {
    CLIP(1),
    ELLIPSIS(2);

    companion object {
        fun fromInt(value: Int) = entries.firstOrNull { it.value == value } ?: ELLIPSIS
        fun ListNoteContentOverflowStyle.toInt() = value
    }
}