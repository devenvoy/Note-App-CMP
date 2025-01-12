package com.devansh.noteapp.ui.composable.home.notes

import com.devansh.noteapp.domain.model.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val searchText: String = "",
    val isSearchActive: Boolean = false
)
