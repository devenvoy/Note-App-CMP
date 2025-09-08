package com.devansh.noteapp.domain.model

import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import com.devansh.noteapp.CategoryEntity
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String = "",
    val name: String = "",
    val color: Long? = null,
    val isSynced: Boolean = false,
    val notesCount: Long = 0
) {
    companion object Companion {
        val folderColors = listOf(Red, Yellow, Green, Cyan, Blue)
    }
}

fun CategoryEntity.toCategory() =
    Category(id = categoryId, name = categoryName, color = colorRes, isSynced = isSynced == 1L)
