package com.devansh.noteapp.ui.screens.home.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.ui.components.NoteItemUI


@Composable
fun NoteScreenContent(
    state: NoteListState,
    onNavigateToAddEditNote: (Long) -> Unit,
    isGridLayout: Boolean,
    onDeleteNote: (Long) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {

        /* AnimatedVisibility(
             visible = state.isOrderSectionVisible,
             enter = fadeIn() + slideInVertically(),
             exit = fadeOut() + slideOutVertically()
         ) {
             OrderSection(
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 16.dp),
                 noteOrder = state.noteOrder,
                 onOrderChange = {               }
             )
         }*/

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
                        .clickable {
                            onNavigateToAddEditNote(note.id ?: -1)
                        },
                    onShareClicked = { },
                    onDeleteClicked = { note.id?.let { onDeleteNote(it) } }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}