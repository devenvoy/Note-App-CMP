package com.devansh.noteapp.ui.screens.add_edit_note

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.model.Note.Companion.emptyNote
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.repo.NoteService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AddEditNoteViewModel(
    private val currentNoteId: String? = null,
    private val pref: AppCacheSetting,
    private val noteDataSource: NoteDataSource,
    private val noteService: NoteService
) : ViewModel() {

    val noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title"))

    private val _currentNote: MutableStateFlow<Note> = MutableStateFlow(emptyNote())
    val currentNote = _currentNote.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    private val contentUpdateJob = mutableStateOf<Job?>(null)

    init {
        if (currentNoteId != null) {
            viewModelScope.launch {
                noteDataSource.getNoteById(currentNoteId)?.also { note ->

                    noteTitle.value = noteTitle.value.copy(
                        text = note.title,
                        isHintVisible = note.title.isBlank()
                    )

                    _currentNote.update { note }
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                noteTitle.value = noteTitle.value.copy(text = event.newTitle)
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
                _currentNote.update { it.copy(title = noteTitle.value.text) }
            }

            is AddEditNoteEvent.EnteredContent -> {
                contentUpdateJob.value?.cancel()
                _currentNote.update { it.copy(content = event.newContent) }
                contentUpdateJob.value = viewModelScope.launch {
                    delay(200)
                    onEvent(AddEditNoteEvent.SaveNote)
                }
            }

            is AddEditNoteEvent.ChangeContentFocus -> {}

            is AddEditNoteEvent.ChangeColor -> {
                _currentNote.update { it.copy(colorRes = event.color) }
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        val note = validateAndGetNote()
                        note?.let { noteDataSource.insertNote(note, false) }
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
            currentNoteId?.let {
                noteService.deleteNote(it, pref.accessToken.toString())
                noteDataSource.deleteNoteById(it)
                _eventFlow.emit(UiEvent.ShowSnackbar("Note Deleted"))
                delay(2000)
                _eventFlow.emit(UiEvent.Navigate())
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun validateAndGetNote(): Note? {
        val currentNoteValue = _currentNote.value

        if (currentNoteValue.content.isBlank()) {
            _eventFlow.emit(UiEvent.ShowSnackbar("Note cannot be empty"))
            return null
        }

        val currentTime = Clock.System.now().toString()

        return Note(
            id = currentNoteId,
            title = currentNoteValue.title,
            content = currentNoteValue.content,
            category = currentNoteValue.category,
            colorRes = currentNoteValue.colorRes,
            createdAt = currentNoteValue.createdAt.takeIf { currentNoteId != null } ?: currentTime,
            updatedAt = currentTime,
        )
    }
}

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data class Navigate(val route: Any? = null) : UiEvent
}