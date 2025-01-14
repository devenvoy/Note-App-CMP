package com.devansh.noteapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.devansh.noteapp.domain.utils.koinScreenModel
import com.devansh.noteapp.ui.components.ExpandableSearchView
import com.devansh.noteapp.ui.screens.add_edit_note.AddEditNoteScreen
import com.devansh.noteapp.ui.screens.home.notes.NoteScreenContent
import com.devansh.noteapp.ui.theme.getMontBFont
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Plus
import network.chaintech.sdpcomposemultiplatform.ssp

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val homeScreenModel = koinScreenModel<HomeScreenModel>()
        HomeScreenContent(
            homeScreenModel = homeScreenModel,
            onNavigateToAddEditNote = {
                navigator.push(AddEditNoteScreen(it))
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    homeScreenModel: HomeScreenModel,
    onNavigateToAddEditNote: (Long) -> Unit,
) {

    val noteState by homeScreenModel.noteState.collectAsState()
    var isGridLayout by remember{ mutableStateOf(false) }
    Scaffold(topBar = {
        ExpandableSearchView(
            modifier = Modifier.fillMaxWidth(),
            expandedInitially = noteState.isSearchActive,
            onExpandedChanged = { b -> homeScreenModel.onToggleSearch() },
            searchDisplay = noteState.searchText,
            onSearchDisplayChanged = {
                homeScreenModel.onSearchTextChange(it)
            },
            onSearch = {
                homeScreenModel.onSearchTextChange("")
            },
        ) {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                Text(text = "Notes", fontSize = 16.ssp, fontFamily = getMontBFont())
            }, actions = {
                IconButton(onClick = {
                    homeScreenModel.onToggleSearch()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Search, contentDescription = "search"
                    )
                }
                IconButton(onClick = {
                    isGridLayout = !isGridLayout
                }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = if(isGridLayout) Icons.Filled.Menu else Icons.AutoMirrored.Filled.List,
                        contentDescription = "Add"
                    )
                }
            })
        }
    },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.imePadding(),
                onClick = { onNavigateToAddEditNote(-1) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = FontAwesomeIcons.Solid.Plus,
                    contentDescription = "Save Note"
                )
            }
        }) {
        Column(
            modifier = Modifier.padding(it).fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoteScreenContent(
                state = noteState,
                onNavigateToAddEditNote = onNavigateToAddEditNote,
                isGridLayout = isGridLayout,
                onDeleteNote = { id ->
                    homeScreenModel.deleteNoteById(id)
                })
        }
    }
}
