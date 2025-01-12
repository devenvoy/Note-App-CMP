package com.devansh.noteapp.ui.screens.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.utils.DateTimeUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddEditNoteViewModel(
    private val noteDataSource: NoteDataSource
) : ScreenModel {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title"))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf("")
    val noteContent = _noteContent

    private val _noteColor = MutableStateFlow(Note.generateRandomColor())
    val noteColor = _noteColor.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Long? = null

    fun initState(noteId: Long) {
        if (noteId != -1L) {
            screenModelScope.launch {
                noteDataSource.getNoteById(noteId)?.also { note ->
                    currentNoteId = note.id
                    _noteTitle.value = noteTitle.value.copy(
                        text = note.title,
                        isHintVisible = false
                    )

                    _noteContent.value = note.content

                    _noteColor.value = note.colorRes
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
             is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(text = event.newTitle)
            }

             is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value =  event.newContent
            }

            is AddEditNoteEvent.ChangeContentFocus -> {

            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            is AddEditNoteEvent.SaveNote -> {
                screenModelScope.launch {
                    try {
                        noteDataSource.insertNote(
                            Note(
                                title = noteTitle.value.text,
                                content = event.content,
                                created = DateTimeUtil.now(),
                                colorRes = noteColor.value,
                                id = currentNoteId
                            )
                        )
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

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object SaveNote : UiEvent()
    }

}