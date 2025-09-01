package com.devansh.noteapp.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.repo.NoteRemoteService
import com.devansh.noteapp.domain.repo.SearchNotes
import com.devansh.noteapp.domain.utils.onFailure
import com.devansh.noteapp.domain.utils.onSuccess
import com.devansh.noteapp.ui.screens.home.notes.NoteListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val pref: AppCacheSetting,
    private val noteDataSource: NoteDataSource,
    private val noteRemoteService: NoteRemoteService
) : ViewModel() {

    // use case
    private val searchNotes = SearchNotes()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    private val searchText = MutableStateFlow("")
    private val isSearchActive = MutableStateFlow(false)

    val isGridLayout = pref.observableListType
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    var isRefreshing = mutableStateOf(false)

    val noteState =
        combine(_notes, searchText, isSearchActive) { list, text, isSearchActive ->
            NoteListState(
                notes = searchNotes.execute(list, text),
                searchText = text,
                isSearchActive = isSearchActive
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), NoteListState())


    init {
        getAllNotes()
        viewModelScope.launch {
            noteDataSource.getAllNotes().collect { newList -> _notes.update { newList } }
        }
    }

    fun onSearchTextChange(text: String) {
        searchText.update { text }
    }

    fun onToggleSearch() {
        isSearchActive.update { !it }
        if (!isSearchActive.value) {
            searchText.update { "" }
        }
    }

    fun deleteNoteById(id: String) {
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
            noteRemoteService.deleteNote(id, pref.accessToken.toString())
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            try {
                isRefreshing.value = true
                val result = noteRemoteService.getNotes(pref.accessToken.toString())

                result.onSuccess { response ->
                    response.forEach { note -> noteDataSource.insertNote(note, true) }
                }.onFailure {
                    Logger.e("SyncError", null) { "Failed to sync data" }
                }

            } catch (e: Exception) {
                Logger.e("SyncError", e) { "Unexpected error occurred during sync" }
            } finally {
                isRefreshing.value = false
            }
        }
    }
}