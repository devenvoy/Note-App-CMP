package com.devansh.noteapp.feature.notes.presentation.add_edit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.core.database.repo.NoteDataSource
import com.devansh.noteapp.data.models.dto.NoteResponse
import com.devansh.noteapp.data.models.dto.NoteResponse.Companion.emptyNote
import com.devansh.noteapp.data.repository.repo.NoteService
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
    private val currentNoteId: Long,
    private val noteDataSource: NoteDataSource,
    private val noteService: NoteService
) : ViewModel() {

    val noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title"))

    private val _currentNoteResponse: MutableStateFlow<NoteResponse> = MutableStateFlow(emptyNote())
    val currentNote = _currentNoteResponse.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    private val contentUpdateJob = mutableStateOf<Job?>(null)

    init {
        viewModelScope.launch {
            noteDataSource.getNoteById(currentNoteId)?.also { note ->

                noteTitle.value = noteTitle.value.copy(
                    text = note.title,
                    isHintVisible = note.title.isBlank()
                )

                _currentNoteResponse.update { note }
            } ?: run {
                _currentNoteResponse.update { emptyNote() }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.OnTitleChange -> {
                noteTitle.value = noteTitle.value.copy(text = event.newTitle)
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
                _currentNoteResponse.update { it.copy(title = noteTitle.value.text) }
            }

            is AddEditNoteEvent.OnContentChange -> {
                contentUpdateJob.value?.cancel()
                _currentNoteResponse.update { it.copy(content = event.newContent) }
                contentUpdateJob.value = viewModelScope.launch {
                    delay(200)
                    onEvent(AddEditNoteEvent.SaveNote)
                }
            }

            is AddEditNoteEvent.ChangeContentFocus -> {}

            is AddEditNoteEvent.OnColorChange -> {
                _currentNoteResponse.update { it.copy(colorRes = event.color) }
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
            _currentNoteResponse.value.noteId?.let {
                noteService.deleteNote(it)
                noteDataSource.deleteNoteById(it)
                _eventFlow.emit(UiEvent.ShowSnackbar("Note Deleted"))
                delay(2000)
                _eventFlow.emit(UiEvent.Navigate())
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun validateAndGetNote(): NoteResponse? {
        val currentNoteValue = _currentNoteResponse.value

        if (currentNoteValue.content.isBlank()) {
            _eventFlow.emit(UiEvent.ShowSnackbar("Note cannot be empty"))
            return null
        }

        val currentTime = Clock.System.now().toString()

        return NoteResponse(
            id = currentNoteValue.id,
            noteId = currentNoteValue.noteId,
            title = currentNoteValue.title,
            content = currentNoteValue.content,
            category = currentNoteValue.category,
            colorRes = currentNoteValue.colorRes,
            createdAt = currentNoteValue.createdAt.takeIf { currentNoteId != 0L } ?: currentTime,
            updatedAt = currentTime,
        )
    }
}

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data class Navigate(val route: Any? = null) : UiEvent
}