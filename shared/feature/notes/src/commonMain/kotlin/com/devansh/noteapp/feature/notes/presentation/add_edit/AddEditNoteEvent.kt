package com.devansh.noteapp.feature.notes.presentation.add_edit

import androidx.compose.ui.focus.FocusState
import com.devansh.noteapp.data.models.dto.Category

sealed interface AddEditNoteEvent {
    data class OnTitleChange(val newTitle: String) : AddEditNoteEvent
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent
    data class OnContentChange(val newContent: String) : AddEditNoteEvent
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent
    data class OnColorChange(val color: Long?) : AddEditNoteEvent

    data class OnCategoryChange(val category: Category) : AddEditNoteEvent
    data object SaveNote : AddEditNoteEvent
}