package com.devansh.noteapp.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetNotesResponse(
    @SerialName("notes")
    val notes: List<Note> = emptyList(),
    @SerialName("total")
    val total: Int = 0
)