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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    @SerialName("id") val id: Long?,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("colorRes") val colorRes: Long,
    @SerialName("category") val category: String?,
    @SerialName("modifiedAt") val lastModified: LocalDateTime,
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
    category = category,
    lastModified = Instant.fromEpochMilliseconds(last_modified)
        .toLocalDateTime(TimeZone.currentSystemDefault())
)