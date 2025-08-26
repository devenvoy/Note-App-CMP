package com.devansh.noteapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.repo.SearchNotes
import com.devansh.noteapp.ui.screens.home.notes.NoteListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val noteDataSource: NoteDataSource,
) : ViewModel() {

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
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), NoteListState())


    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
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
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
            loadNotes()
        }
    }
}