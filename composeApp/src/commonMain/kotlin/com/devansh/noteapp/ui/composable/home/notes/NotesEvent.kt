package com.devansh.noteapp.ui.composable.home.notes

import com.devansh.noteapp.domain.model.Note

sealed class NotesEvent {
//    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    data object RestoreNote : NotesEvent()
    data object ToggleOrderSection : NotesEvent()
}
