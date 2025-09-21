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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.data.models.dto.NoteResponse


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteScreenContent(
    state: NoteListState,
    onNavigateToAddEditNote: (Long) -> Unit,
    isGridLayout: Boolean,
    onLongPress: (NoteResponse) -> Unit
) {

    val window = currentWindowAdaptiveInfo()

    var gridCells by rememberSaveable(window, isGridLayout) {
        val isDesktop = window.windowSizeClass.isWidthAtLeastBreakpoint(1440)
        val isTablet = window.windowSizeClass.isWidthAtLeastBreakpoint(720)

        mutableStateOf(
            if (isGridLayout) {
                when {
                    isDesktop -> 4
                    isTablet -> 3
                    else -> 2
                }
            } else 1
        )
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(gridCells),
        modifier = Modifier.fillMaxSize()
    ) {
        items(state.noteResponses) { note ->
            NoteItemUI(
                noteResponse = note,
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