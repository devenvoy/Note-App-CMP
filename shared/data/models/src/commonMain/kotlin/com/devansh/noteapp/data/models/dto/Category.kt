package com.devansh.noteapp.data.models.dto

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

    constructor(
        id: String,
        name: String,
        color: Long?,
        createdAt: String? = null,
        updatedAt: String? = null
    ) : this(id, name, color) {
        this.createdAt = createdAt
        this.updatedAt = updatedAt
    }

    fun updateNoteCount(noteCount: Long) {
        this.notesCount = noteCount
    }

    companion object {
        fun emptyCategory() = Category(name = "Uncategorized", color = 4286611584, isSynced = true)
    }
}
