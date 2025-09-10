package com.devansh.noteapp.domain.model

import com.devansh.noteapp.NoteEntity
import com.devansh.noteapp.ui.theme.BabyBlueHex
import com.devansh.noteapp.ui.theme.LightGreenHex
import com.devansh.noteapp.ui.theme.RedOrangeHex
import com.devansh.noteapp.ui.theme.RedPinkHex
import com.devansh.noteapp.ui.theme.VioletHex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    @SerialName("id") val id: String?,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("color") val colorRes: Long,
    @SerialName("categoryId") val category: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
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
    colorRes = colorRes,
    category = categoryId,
    createdAt = createdAt,
    updatedAt = updatedAt
)