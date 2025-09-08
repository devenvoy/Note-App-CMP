package com.devansh.noteapp.ui.screens.add_edit_note

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.repo.NoteService
import com.devansh.noteapp.domain.utils.onFailure
import com.devansh.noteapp.domain.utils.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddEditNoteViewModel(
    private val pref: AppCacheSetting,
    private val noteDataSource: NoteDataSource,
    private val noteService: NoteService
) : ViewModel() {

    val noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title"))

    val noteContent = mutableStateOf("")

    private val _noteColor = MutableStateFlow(Note.generateRandomColor())
    val noteColor = _noteColor.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: String? = null

    fun initState(noteId: String?) {
        if (noteId != null) {
            viewModelScope.launch {
                noteDataSource.getNoteById(noteId)?.also { note ->
                    currentNoteId = note.id

                    noteTitle.value =
                        noteTitle.value.copy(text = note.title, isHintVisible = false)

                    noteContent.value = note.content

                    _noteColor.value = note.colorRes
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                noteTitle.value = noteTitle.value.copy(text = event.newTitle)
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                noteContent.value = event.newContent
            }

            is AddEditNoteEvent.ChangeContentFocus -> {

            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        val note = Note(
                            id = currentNoteId,
                            title = noteTitle.value.text,
                            content = event.content,
                            category = null,
                            colorRes = noteColor.value,
                        )
                        noteService.upsert(note, pref.accessToken.toString())
                            .onSuccess { noteDataSource.insertNote(it, true) }
                            .onFailure { noteDataSource.insertNote(note, false) }
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(message = e.message ?: "Couldn't Save Note")
                        )
                    }
                }
            }
        }
    }

    fun deleteNoteById() {
        viewModelScope.launch {
            currentNoteId?.let { noteDataSource.deleteNoteById(it) }
        }
    }

    sealed interface UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent
        data object SaveNote : UiEvent
    }

}