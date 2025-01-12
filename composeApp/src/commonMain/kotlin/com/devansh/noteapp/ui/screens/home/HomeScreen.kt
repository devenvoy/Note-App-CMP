package com.devansh.noteapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.devansh.noteapp.ui.components.ExpandableSearchView
import com.devansh.noteapp.ui.screens.add_edit_note.AddEditNoteScreen
import com.devansh.noteapp.ui.screens.core.Constants
import com.devansh.noteapp.ui.screens.home.notes.NoteScreenContent
import com.devansh.noteapp.ui.theme.getMontBFont
import network.chaintech.sdpcomposemultiplatform.ssp

class HomeScreen : Screen {
    @Composable
    override fun Content() {
val navigator = LocalNavigator.currentOrThrow
        val homeScreenModel = rememberScreenModel { HomeScreenModel(Constants.noteDatabase) }
        HomeScreenContent(
            homeScreenModel = homeScreenModel,
            onNavigateToAddEditNote={
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
                    onNavigateToAddEditNote(-1)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add, contentDescription = "Add"
                    )
                }
            })
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
               onNavigateToAddEditNote =onNavigateToAddEditNote,
               onDeleteNote = { id->
                   homeScreenModel.deleteNoteById(id)
               }
           )
        }
    }
}
