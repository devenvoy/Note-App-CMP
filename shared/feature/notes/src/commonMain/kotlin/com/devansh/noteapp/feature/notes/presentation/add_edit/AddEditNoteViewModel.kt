package com.devansh.noteapp.feature.notes.presentation.add_edit

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.core.database.repo.CategoryDataSource
import com.devansh.noteapp.core.database.repo.NoteDataSource
import com.devansh.noteapp.data.models.dto.Category
import com.devansh.noteapp.data.models.dto.Category.Companion.emptyCategory
import com.devansh.noteapp.data.models.dto.NoteResponse
import com.devansh.noteapp.data.models.dto.NoteResponse.Companion.emptyNote
import com.devansh.noteapp.data.repository.repo.NoteService
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
    private val categoryDataSource: CategoryDataSource,
    private val noteService: NoteService
) : ViewModel() {

    val richTextState = RichTextState()
    private var contentUpdateJob: Job? = null
    private var titleUpdateJob: Job? = null
    val noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title"))
    private val _currentNoteResponse: MutableStateFlow<NoteResponse> = MutableStateFlow(emptyNote())
    val currentNote = _currentNoteResponse.asStateFlow()

    private val _categories = MutableStateFlow(emptyList<Category>())
    val categories = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category>(emptyCategory())
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        setupRichTextConfig()
        loadCategories()
        loadNote()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryDataSource.getAllCategories().collect { list ->
                _categories.update { list }
            }
        }
    }

    private fun setupRichTextConfig() {
        richTextState.config.apply {
            linkColor = Color.Blue
            linkTextDecoration = TextDecoration.Underline
            codeSpanColor = Color.Yellow
            codeSpanBackgroundColor = Color.Transparent
            codeSpanStrokeColor = Color.LightGray
        }
    }

    private fun loadNote() {
        viewModelScope.launch {
            noteDataSource.getNoteById(currentNoteId)?.also { note ->
                noteTitle.value =
                    noteTitle.value.copy(text = note.title, isHintVisible = note.title.isBlank())

                _currentNoteResponse.update { note }

                categoryDataSource.getCategoryById(note.category ?: "")
                    ?.let { cat -> _selectedCategory.update { cat } }

                richTextState.setHtml(note.content)
            } ?: run {
                _currentNoteResponse.update { emptyNote() }
                _selectedCategory.update { emptyCategory() }
            }
        }
    }

    private fun updateNoteContent(newContent: String) {
        contentUpdateJob?.cancel()
        _currentNoteResponse.update { it.copy(content = newContent) }

        contentUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
//            onEvent(AddEditNoteEvent.SaveNote)
            saveNote()
        }
    }

    private fun updateNoteTitle(newTitle: String) {
        titleUpdateJob?.cancel()
        _currentNoteResponse.update { it.copy(title = newTitle) }

        titleUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            delay(300) // Shorter delay for title changes
            saveNote() // Auto-save when title changes
        }
    }


    @OptIn(ExperimentalTime::class)
    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.OnTitleChange -> {
                noteTitle.value = noteTitle.value.copy(text = event.newTitle)
                updateNoteTitle(event.newTitle)
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
                _currentNoteResponse.update { it.copy(title = noteTitle.value.text) }
                if (!event.focusState.isFocused) {
                    viewModelScope.launch { saveNote() }
                }
            }

            is AddEditNoteEvent.OnContentChange -> {
//                richTextState.setHtml(event.newContent)
                updateNoteContent(richTextState.toHtml())
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                if (!event.focusState.isFocused) {
                    viewModelScope.launch { saveNote() }
                }
            }

            is AddEditNoteEvent.OnColorChange -> {
                _currentNoteResponse.update { it.copy(colorRes = event.color) }
                updateNoteContent(richTextState.toHtml())
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch { saveNote() }
            }

            is AddEditNoteEvent.OnCategoryChange -> {
                _selectedCategory.update { event.category }
                _currentNoteResponse.update { it.copy(category = event.category.id) }
            }
        }
    }

    private suspend fun saveNote() {
        try {
            val note = validateAndGetNote()
            note?.let {
                noteDataSource.insertNote(note, false)
                // Optional: Emit success event
//                _eventFlow.emit(UiEvent.ShowSnackbar("Note saved"))
            }
        } catch (e: Exception) {
            _eventFlow.emit(
                UiEvent.ShowSnackbar(
                    message = e.message ?: "Couldn't Save Note"
                )
            )
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

    fun forceSave() {
        viewModelScope.launch { saveNote() }
    }

    override fun onCleared() {
        super.onCleared()
        // Cancel any pending jobs
        contentUpdateJob?.cancel()
        titleUpdateJob?.cancel()
    }

    fun addCategory(item: Category) {
        viewModelScope.launch {
            categoryDataSource.insertCategory(item, true)
            _selectedCategory.update { item }
        }
    }
}

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data class Navigate(val route: Any? = null) : UiEvent
}