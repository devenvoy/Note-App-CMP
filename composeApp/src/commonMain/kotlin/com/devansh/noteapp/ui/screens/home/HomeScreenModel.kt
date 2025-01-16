package com.devansh.noteapp.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import com.devansh.noteapp.data.remote.utils.onError
import com.devansh.noteapp.data.remote.utils.onSuccess
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.repo.NoteRemoteDao
import com.devansh.noteapp.domain.repo.SearchNotes
import com.devansh.noteapp.ui.screens.home.notes.NoteListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val pref: AppCacheSetting,
    private val noteDataSource: NoteDataSource,
    private val noteRemoteDao: NoteRemoteDao
) : ScreenModel {

    // use case
    private val searchNotes = SearchNotes()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    private val searchText = MutableStateFlow("")
    private val isSearchActive = MutableStateFlow(false)

    var isGridLayout =  mutableStateOf(false)
    var isRefreshing =  mutableStateOf(false)

    val noteState =
        combine(_notes, searchText, isSearchActive) { list, text, isSearchActive ->
            NoteListState(
                notes = searchNotes.execute(list, text),
                searchText = text,
                isSearchActive = isSearchActive
            )
        }.stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000L), NoteListState())


    init {
        getAllNotes()
        loadNotes()
    }

    private fun loadNotes() {
        screenModelScope.launch {
            noteDataSource.getAllNotes().collect { newList ->
                _notes.update { newList }
            }
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

    fun deleteNoteById(id: Long) {
        screenModelScope.launch {
            noteDataSource.deleteNoteById(id)
            loadNotes()
        }
    }

    fun getAllNotes() {
        screenModelScope.launch {
            try {
                isRefreshing.value = true
                val result = noteRemoteDao.getNotes(pref.accessToken)

                result.onSuccess { response ->
                    if (response.status) {
                        response.value?.notes?.forEach { note ->
                            noteDataSource.insertNote(note, true)
                        }
                    } else {
                        Logger.e("SyncError", null) { "${response.detail}" }
                        Logger.e("SyncError", null) { "Failed to sync data" }
                    }
                }.onError {
                    Logger.e("SyncError", null) { "Failed to sync data" }
                }

            }catch (e:Exception){
                Logger.e("SyncError", e) { "Unexpected error occurred during sync" }
            }finally {
                isRefreshing.value=false
            }
        }
    }
}