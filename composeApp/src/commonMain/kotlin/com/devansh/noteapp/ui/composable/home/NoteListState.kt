package com.devansh.noteapp.ui.composable.home

import com.devansh.noteapp.domain.model.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val searchText: String = "",
    val isSearchActive: Boolean = false
)
