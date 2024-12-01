package com.devansh.noteapp.domain.model

import com.devansh.noteapp.NoteEntity
import com.devansh.noteapp.ui.theme.BabyBlueHex
import com.devansh.noteapp.ui.theme.LightGreenHex
import com.devansh.noteapp.ui.theme.RedOrangeHex
import com.devansh.noteapp.ui.theme.RedPinkHex
import com.devansh.noteapp.ui.theme.VioletHex
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Note(
    val id: Long?,
    val title: String,
    val content: String,
    val colorRes: Long,
    val created: LocalDateTime
) {
    companion object {
        val colors = listOf(
            RedOrangeHex,
            RedPinkHex,
            BabyBlueHex,
            VioletHex,
            LightGreenHex
        )

        fun generateRandomColor() = colors.random()
    }
}


fun NoteEntity.toNote() = Note(
    id = id,
    title = title,
    content = content,
    colorRes = colorres,
    created = Instant.fromEpochMilliseconds(created)
        .toLocalDateTime(TimeZone.currentSystemDefault())
)