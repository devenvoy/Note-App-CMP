package com.devansh.noteapp.ui.composable.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.repo.SearchNotes
import com.devansh.noteapp.ui.composable.home.notes.NoteListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val noteDataSource: NoteDataSource,
) : ScreenModel {

    // use case
    private val searchNotes = SearchNotes()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    private val searchText = MutableStateFlow("")
    private val isSearchActive = MutableStateFlow(false)

    val noteState =
        combine(_notes, searchText, isSearchActive) { list, text, isSearchActive ->
            NoteListState(
                notes = searchNotes.execute(list, text),
                searchText = text,
                isSearchActive = isSearchActive
            )
        }.stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000L), NoteListState())


    init {
        loadNotes()
    }

    private fun loadNotes() {
        screenModelScope.launch {
            noteDataSource.getAllNotes().collect{ newList->
            _notes.update { newList }
            }
        }
    }

    fun onSearchTextChange(text: String) {
       searchText.update { text }
    }

    fun onToggleSearch(){
        isSearchActive.update { !it }
        if(!isSearchActive.value){
            searchText.update { "" }
        }
    }

    fun deleteNoteById(id: Long){
        screenModelScope.launch {
            noteDataSource.deleteNoteById(id)
            loadNotes()
        }
    }
}