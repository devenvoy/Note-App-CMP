package com.devansh.noteapp.feature.notes.presentation.notes

import com.devansh.noteapp.data.models.dto.NoteResponse

sealed class NotesEvent {
//    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class DeleteNote(val noteResponse: NoteResponse) : NotesEvent()
    data object RestoreNote : NotesEvent()
    data object ToggleOrderSection : NotesEvent()
}
