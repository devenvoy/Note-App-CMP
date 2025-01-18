package com.devansh.noteapp.ui.screens.home.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.domain.model.Note


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteScreenContent(
    state: NoteListState,
    onNavigateToAddEditNote: (Long) -> Unit,
    isGridLayout: Boolean,
    onLongPress: (Note) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(if (isGridLayout) 2 else 1),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.notes) { note ->
                NoteItemUI(
                    note = note,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                onNavigateToAddEditNote(note.id ?: -1)
                            },
                            onLongClick = {
                                onLongPress(note)
                            })
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}