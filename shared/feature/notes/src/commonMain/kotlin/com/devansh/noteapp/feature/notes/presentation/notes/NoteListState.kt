package com.devansh.noteapp.feature.notes.presentation.notes

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.devansh.noteapp.data.models.dto.NoteResponse
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentOverflowStyle
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentOverflowStyle.CLIP
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentOverflowStyle.ELLIPSIS
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentSize

data class NoteListState(
    val noteResponses: List<NoteResponse> = emptyList(),
    val searchText: String = "",
    val isSearchActive: Boolean = false,
)

fun ListNoteContentOverflowStyle.toTextOverFlow(): TextOverflow = when (this) {
    CLIP -> TextOverflow.Clip
    ELLIPSIS -> TextOverflow.Ellipsis
}

fun Int.toTextAlign(): TextAlign = when (this) {
    0 -> TextAlign.Start
    1 -> TextAlign.Center
    2 -> TextAlign.End
    else -> TextAlign.Unspecified
}

fun ListNoteContentSize.toMaxLines(): Int = when (this) {
    ListNoteContentSize.DEFAULT -> 12
    ListNoteContentSize.COMPACT -> 6
    ListNoteContentSize.FLAT -> Int.MAX_VALUE
}