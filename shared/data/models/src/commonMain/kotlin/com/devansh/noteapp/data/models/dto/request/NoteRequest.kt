
package com.devansh.noteapp.data.models.dto.request

data class NoteRequest(
    val id: String?,
    val title: String,
    val content : String,
    val color: Long?,
)