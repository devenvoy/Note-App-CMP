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
) {
    var notesCount: Long = 0
        private set
    var createdAt: String? = null
        private set
    var updatedAt: String? = null
        private set

    companion object Companion {
        val folderColors = listOf(Red, Yellow, Green, Cyan, Blue)
    }

    constructor(
        id: String,
        name: String,
        color: Long?,
        isSynced: Boolean,
        notesCount: Long = 0,
        createdAt: String? = null,
        updatedAt: String? = null
    ) : this(id, name, color, isSynced) {
        this.notesCount = notesCount
        this.createdAt = createdAt
        this.updatedAt = updatedAt
    }
}

fun CategoryEntity.toCategory() =
    Category(id = categoryId, name = categoryName, color = colorRes, isSynced = isSynced == 1L)
