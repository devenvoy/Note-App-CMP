package com.devansh.noteapp.domain.entity

import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import kotlinx.serialization.Serializable

@Serializable
data class CategoryEntity(
    val id: String? = null,
    val name: String = "",
    val color: Int? = null
) {
    companion object Companion {
        val folderColors = listOf(
            Red,
            Yellow,
            Green,
            Cyan,
            Blue
        )
    }
}