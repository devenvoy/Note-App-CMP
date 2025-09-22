package com.devansh.noteapp.feature.notes.presentation.notes

import com.devansh.noteapp.data.models.dto.NoteResponse

data class NoteListState(
    val noteResponses: List<NoteResponse> = emptyList(),
    val searchText: String = "",
    val isSearchActive: Boolean = false
)
