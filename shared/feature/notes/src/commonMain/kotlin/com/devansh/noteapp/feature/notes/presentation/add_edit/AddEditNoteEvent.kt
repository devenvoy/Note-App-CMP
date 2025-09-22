package com.devansh.noteapp.feature.notes.presentation.add_edit

import androidx.compose.ui.focus.FocusState

sealed interface AddEditNoteEvent {
    data class OnTitleChange(val newTitle: String) : AddEditNoteEvent
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent
    data class OnContentChange(val newContent: String) : AddEditNoteEvent
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent
    data class OnColorChange(val color: Long) : AddEditNoteEvent
    data object SaveNote : AddEditNoteEvent
}