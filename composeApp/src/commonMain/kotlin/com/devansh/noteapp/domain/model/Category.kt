package com.devansh.noteapp.domain.model

import androidx.compose.ui.graphics.Color
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
        val folderColors = listOf(
            Color(0xFFFF5722), // Red-Orange
            Color(0xFF2196F3), // Blue
            Color(0xFF4CAF50), // Green
            Color(0xFFFF9800), // Orange
            Color(0xFF9C27B0), // Purple
            Color(0xFF00BCD4), // Cyan
            Color(0xFFFFEB3B), // Yellow
            Color(0xFFE91E63), // Pink
            Color(0xFF795548), // Brown
            Color(0xFF607D8B), // Blue Grey
        )
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
