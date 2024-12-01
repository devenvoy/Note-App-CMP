package com.devansh.noteapp.ui.composable.add_edit_note

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.utils.DateTimeUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddEditNoteViewModel(
    private val noteDataSource: NoteDataSource
) : ScreenModel {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title"))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Enter some content"))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.generateRandomColor())
    val noteColor: MutableState<Long> = _noteColor

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

                    _noteContent.value = noteContent.value.copy(
                        text = note.content,
                        isHintVisible = false
                    )

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
                _noteContent.value = noteContent.value.copy(text = event.newContent)
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
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
                                content = noteContent.value.text,
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