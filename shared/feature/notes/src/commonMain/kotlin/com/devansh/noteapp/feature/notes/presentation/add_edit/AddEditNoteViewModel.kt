package com.devansh.noteapp.feature.notes.presentation.add_edit

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.devansh.noteapp.core.database.repo.CategoryDataSource
import com.devansh.noteapp.core.database.repo.NoteDataSource
import com.devansh.noteapp.core.utils.IO
import com.devansh.noteapp.core.utils.onFailure
import com.devansh.noteapp.core.utils.onSuccess
import com.devansh.noteapp.data.models.dto.Category
import com.devansh.noteapp.data.models.dto.Category.Companion.emptyCategory
import com.devansh.noteapp.data.models.dto.NoteResponse
import com.devansh.noteapp.data.models.dto.NoteResponse.Companion.emptyNote
import com.devansh.noteapp.data.repository.repo.NoteService
import com.mohamedrejeb.richeditor.model.RichTextState
//import dev.tmapps.konnection.Konnection
import kotlinx.coroutines.Dispatchers
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

@OptIn(ExperimentalTime::class)
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
    private val _selectedCategory = MutableStateFlow(emptyCategory())
    val selectedCategory = _selectedCategory.asStateFlow()
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()
    private fun isOnline(): Boolean = true//Konnection.instance.isConnected()

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
            if (currentNoteId == -1L) {
                val newNote = createNewNote()
                _currentNoteResponse.update { newNote }
                _selectedCategory.update { emptyCategory() }
                return@launch
            }
            noteDataSource.getNoteById(currentNoteId)?.also { note ->
                noteTitle.value = noteTitle.value.copy(
                    text = note.title,
                    isHintVisible = note.title.isBlank()
                )
                _currentNoteResponse.update { note }
                if (!note.category.isNullOrBlank()) {
                    categoryDataSource.getCategoryById(note.category!!)?.let { category ->
                        _selectedCategory.update { category }
                    } ?: run {
                        // Category not found, reset to empty
                        _selectedCategory.update { emptyCategory() }
                        Logger.w(
                            "Category ${note.category} not found, resetting",
                            null,
                            "AddEditNote"
                        )
                    }
                } else {
                    _selectedCategory.update { emptyCategory() }
                }
                richTextState.setHtml(note.content)
            } ?: run {
                val newNote = createNewNote()
                _currentNoteResponse.update { newNote }
                _selectedCategory.update { emptyCategory() }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun createNewNote(): NoteResponse {
        val currentTime = Clock.System.now().toString()
        return NoteResponse(
            id = if (currentNoteId > 0) currentNoteId else -1L,
            noteId = null,
            title = "",
            content = "",
            colorRes = null,
            category = null,
            createdAt = currentTime,
            updatedAt = currentTime,
            isSynced = false,
            isDeleted = false,
            pendingDeletion = false
        )
    }

    private fun updateNoteContent(newContent: String) {
        contentUpdateJob?.cancel()
        _currentNoteResponse.update {
            it.copy(
                content = newContent,
                updatedAt = Clock.System.now().toString()
            )
        }

        contentUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            saveNote()
        }
    }

    private fun updateNoteTitle(newTitle: String) {
        titleUpdateJob?.cancel()
        _currentNoteResponse.update {
            it.copy(
                title = newTitle,
                updatedAt = Clock.System.now().toString()
            )
        }

        titleUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            delay(300)
            saveNote()
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
                if (!event.focusState.isFocused) {
                    _currentNoteResponse.update {
                        it.copy(
                            title = noteTitle.value.text,
                            updatedAt = Clock.System.now().toString()
                        )
                    }
                    viewModelScope.launch { saveNote() }
                }
            }

            is AddEditNoteEvent.OnContentChange -> {
                updateNoteContent(richTextState.toHtml())
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                if (!event.focusState.isFocused) {
                    viewModelScope.launch { saveNote() }
                }
            }

            is AddEditNoteEvent.OnColorChange -> {
                _currentNoteResponse.update {
                    it.copy(
                        colorRes = event.color,
                        updatedAt = Clock.System.now().toString()
                    )
                }
                updateNoteContent(richTextState.toHtml())
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch { saveNote() }
            }

            is AddEditNoteEvent.ForceSave -> {
                viewModelScope.launch { saveNote() }
            }

            is AddEditNoteEvent.OnCategoryChange -> {
                _selectedCategory.update { event.category }
                _currentNoteResponse.update {
                    it.copy(
                        category = event.category.id.ifBlank { null },
                        updatedAt = Clock.System.now().toString()
                    )
                }
                viewModelScope.launch { saveNote() }
            }

            is AddEditNoteEvent.AddNewCategory -> {
                addCategory(event.category)
            }
        }
    }

    private suspend fun saveNote() {
        if (_isSaving.value) return

        try {
            _isSaving.value = true
            val note = validateAndGetNote()

            note?.let { validNote ->
                // Ensure category is properly set
                val categoryId =
                    _selectedCategory.value.let { category -> category.id.ifBlank { null } }
                val finalNote = validNote.copy(category = categoryId)
                val insertedId = noteDataSource.insertNote(finalNote, false)

                if (_currentNoteResponse.value.id == -1L) {
                    _currentNoteResponse.update { currentNote -> currentNote.copy(id = insertedId) }
                    Logger.d("New note created with ID: $insertedId", null, "AddEditNote")
                } else {
                    Logger.d("Existing note updated: ${finalNote.id}", null, "AddEditNote")
                }

                Logger.d("Note saved successfully: ${finalNote.id}", null, "AddEditNote")
            }
        } catch (e: Exception) {
            Logger.e("SaveError", e) { "Failed to save note" }
            _eventFlow.emit(
                UiEvent.ShowSnackbar(e.message ?: "Couldn't Save Note")
            )
        } finally {
            _isSaving.value = false
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            try {
                val currentNote = _currentNoteResponse.value

                if (currentNote.id == -1L && currentNote.noteId == null) {
                    // This is a new note that was never saved, just navigate back
                    _eventFlow.emit(UiEvent.ShowSnackbar("Note discarded"))
                    _eventFlow.emit(UiEvent.Navigate())
                    return@launch
                }

                // Use the proper delete logic based on note state
                if (!currentNote.noteId.isNullOrBlank() && isOnline()) {
                    // Online and synced - try server first
                    val result = noteService.deleteNote(currentNote.noteId!!)
                    result.onSuccess {
                        noteDataSource.deleteNoteById(currentNote.id)
                        _eventFlow.emit(UiEvent.ShowSnackbar("Note deleted successfully"))
                        Logger.d(
                            "Note ${currentNote.id} deleted from server and locally",
                            null,
                            "Delete"
                        )
                    }.onFailure {
                        noteDataSource.markForDeletion(currentNote.id)
                        _eventFlow.emit(UiEvent.ShowSnackbar("Note marked for deletion"))
                        Logger.w("Failed to delete from server, marked for sync", null, "Delete")
                    }
                } else if (!currentNote.noteId.isNullOrBlank() && !isOnline()) {
                    // Offline but synced - mark for deletion
                    noteDataSource.markForDeletion(currentNote.id)
                    _eventFlow.emit(UiEvent.ShowSnackbar("Note marked for deletion (offline)"))
                    Logger.d("Offline: Note ${currentNote.id} marked for deletion", null, "Delete")
                } else {
                    // Local-only note - delete immediately
                    noteDataSource.deleteNoteById(currentNote.id)
                    _eventFlow.emit(UiEvent.ShowSnackbar("Note deleted"))
                    Logger.d(
                        "Local-only note ${currentNote.id} deleted immediately",
                        null,
                        "Delete"
                    )
                }

                delay(1500) // Give user time to see the message
                _eventFlow.emit(UiEvent.Navigate())

            } catch (e: Exception) {
                Logger.e("DeleteError", e) { "Failed to delete note" }
                _eventFlow.emit(UiEvent.ShowSnackbar("Failed to delete note: ${e.message}"))
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun validateAndGetNote(): NoteResponse? {
        val currentNoteValue = _currentNoteResponse.value
        val currentTitle = noteTitle.value.text.trim()
        val currentContent = richTextState.toHtml().trim().removeSuffix("<br>")
        if (currentTitle.isBlank() && currentContent.isBlank()) {
            _eventFlow.emit(UiEvent.ShowSnackbar("Note must have either title or content"))
            return null
        }
        val currentTime = Clock.System.now().toString()
        return currentNoteValue.copy(
            title = currentTitle,
            content = currentContent,
            category = _selectedCategory.value.let { it.id.ifBlank { null } },
            createdAt = currentNoteValue.createdAt.takeIf { currentNoteId > 0 } ?: currentTime,
            updatedAt = currentTime,
            isSynced = false
        )
    }
    fun forceSave() {
        viewModelScope.launch { saveNote() }
    }
    fun addCategory(item: Category) {
        viewModelScope.launch {
            try {
                categoryDataSource.insertCategory(item, false)
                _selectedCategory.update { item }
                _currentNoteResponse.update {
                    it.copy(category = item.id, updatedAt = Clock.System.now().toString())
                }
                saveNote()
                Logger.d("Category added and selected: ${item.id}", null, "AddEditNote")
            } catch (e: Exception) {
                Logger.e("CategoryError", e) { "Failed to add category" }
                _eventFlow.emit(UiEvent.ShowSnackbar("Failed to add category: ${e.message}"))
            }
        }
    }

    fun removeCategory() {
        viewModelScope.launch {
            _selectedCategory.update { emptyCategory() }
            _currentNoteResponse.update {
                it.copy(category = null, updatedAt = Clock.System.now().toString())
            }
            saveNote()
        }
    }
    override fun onCleared() {
        super.onCleared()
        contentUpdateJob?.cancel()
        titleUpdateJob?.cancel()
        if (!_isSaving.value) {
            viewModelScope.launch {
                try {
                    saveNote()
                } catch (e: Exception) {
                    Logger.w("Failed to save on clear: ${e.message}", null, "AddEditNote")
                }
            }
        }
    }
}

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data class Navigate(val route: Any? = null) : UiEvent
}