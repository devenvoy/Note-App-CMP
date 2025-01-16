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
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.devansh.noteapp.domain.utils.koinScreenModel
import com.devansh.noteapp.ui.components.ExpandableSearchView
import com.devansh.noteapp.ui.components.MyCustomIndicator
import com.devansh.noteapp.ui.screens.add_edit_note.AddEditNoteScreen
import com.devansh.noteapp.ui.screens.home.notes.NoteScreenContent
import com.devansh.noteapp.ui.theme.getMontBFont
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Plus
import kotlinx.coroutines.launch
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
    val scope = rememberCoroutineScope()
    val noteState by homeScreenModel.noteState.collectAsState()
    val state = rememberPullToRefreshState()

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
                    homeScreenModel.isGridLayout.value = !homeScreenModel.isGridLayout.value
                }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = if (homeScreenModel.isGridLayout.value) Icons.Filled.GridView else Icons.AutoMirrored.Filled.ViewList,
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

        PullToRefreshBox(
            isRefreshing = homeScreenModel.isRefreshing.value,
            onRefresh = {
                scope.launch {
                    homeScreenModel.getAllNotes()
                }
            },
            state = state,
            indicator = {
                MyCustomIndicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = homeScreenModel.isRefreshing.value,
                    state = state
                )
            },
        ) {
            Column(
                modifier = Modifier.padding(it).fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NoteScreenContent(
                    state = noteState,
                    onNavigateToAddEditNote = onNavigateToAddEditNote,
                    isGridLayout = homeScreenModel.isGridLayout.value,
                    onDeleteNote = { id ->
                        homeScreenModel.deleteNoteById(id)
                    }
                )
            }
        }
    }
}
