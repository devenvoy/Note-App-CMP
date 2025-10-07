package com.devansh.noteapp.feature.notes.presentation.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.core.utils.LongCBF
import com.devansh.noteapp.data.models.dto.NoteResponse


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteScreenContent(
    state: NoteListState,
    isGridLayout: Boolean,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign = TextAlign.Unspecified,
    onNavigateToAddEditNote: LongCBF,
    onLongPress: (NoteResponse) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(if (isGridLayout) 2 else 1),
        modifier = Modifier.fillMaxSize()
    ) {
        items(state.noteResponses) { note ->
            NoteItemUI(
                noteResponse = note,
                overflow = overflow,
                maxLines = 8,
                textAlign = textAlign,
                modifier = Modifier
                    .padding(8.dp)
                    .then(
                        if (isGridLayout)
                            Modifier
                        else Modifier.fillMaxWidth()
                    )
                    .combinedClickable(
                        onClick = { onNavigateToAddEditNote(note.id) },
                        onLongClick = { onLongPress(note) }
                    )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}