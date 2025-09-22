package com.devansh.noteapp.data.models.dto.settings;

enum class ListNoteContentSize(private val value: Int) {
    DEFAULT(0),
    COMPACT(1),
    FLAT(2);

    companion object {
        fun fromInt(value: Int) = entries.firstOrNull { it.value == value } ?: DEFAULT
        fun ListNoteContentSize.toInt() = value
    }
}

