package com.devansh.noteapp.ui.composable.add_edit_note

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent {
    data class EnteredTitle(val newTitle: String) : AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()

    data class EnteredContent(val newContent: String) : AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()

    data class ChangeColor(val color: Long) : AddEditNoteEvent()

    data object SaveNote : AddEditNoteEvent()
}