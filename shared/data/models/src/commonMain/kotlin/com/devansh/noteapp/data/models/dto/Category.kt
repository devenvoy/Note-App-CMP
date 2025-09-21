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
