package com.devansh.noteapp.feature.notes.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.devansh.noteapp.core.database.repo.NoteDataSource
import com.devansh.noteapp.core.utils.onFailure
import com.devansh.noteapp.core.utils.onSuccess
import com.devansh.noteapp.data.models.dto.NoteResponse
import com.devansh.noteapp.data.models.dto.request.NoteRequest
import com.devansh.noteapp.data.repository.AppCacheSetting
import com.devansh.noteapp.data.repository.SearchNotes
import com.devansh.noteapp.data.repository.repo.NoteService
import dev.tmapps.konnection.Konnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class HomeScreenViewModel(
    private val pref: AppCacheSetting,
    private val noteDataSource: NoteDataSource,
    private val noteService: NoteService
) : ViewModel() {

    private val searchNotes = SearchNotes()

    private val _notes = MutableStateFlow<List<NoteResponse>>(emptyList())
    private val searchText = MutableStateFlow("")
    private val isSearchActive = MutableStateFlow(false)

    val isGridLayout = pref.listType
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing = _isSyncing.asStateFlow()

    private val _conflictingNotes = MutableStateFlow<List<ConflictPair>>(emptyList())
    val conflictingNotes = _conflictingNotes.asStateFlow()

    val noteState =
        combine(_notes, searchText, isSearchActive) { list, text, isSearchActive ->
            NoteListState(
                noteResponses = searchNotes.execute(list, text),
                searchText = text,
                isSearchActive = isSearchActive
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), NoteListState())


    init {
        getAllNotes()
        viewModelScope.launch {
            noteDataSource.getAllNotes().collect { newList -> _notes.update { newList } }
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


    /**
     * Enhanced delete function with proper offline handling
     */
    fun deleteNoteById(id: String) {
        viewModelScope.launch {
            try {
                if (isOnline()) {
                    // Online - try to delete from server first
                    val result = noteService.deleteNote(id)
                    result.onSuccess {
                        // Successfully deleted from server, remove locally
                        noteDataSource.deleteNoteById(id)
                        Logger.d("Note $id deleted from server and locally",null,"Delete")
                    }.onFailure {
                        // Failed to delete from server, mark for deletion sync later
                        noteDataSource.markForDeletion(id)
                        Logger.w( "Failed to delete from server, marked for sync",null,"Delete")
                    }
                } else {
                    // Offline - mark for deletion when online
                    noteDataSource.markForDeletion(id)
                    Logger.d("Offline: Note $id marked for deletion",null,"Delete")
                }
            } catch (e: Exception) {
                Logger.e("DeleteError", e) { "Failed to delete note: $id" }
                // Mark for deletion sync later
                noteDataSource.markForDeletion(id)
            }
        }
    }

    /**
     * Comprehensive sync function
     */
    fun getAllNotes() {
        viewModelScope.launch {
            if (!isOnline()) {
                Logger.d( "Offline - skipping sync",null,"Sync")
                return@launch
            }

            try {
                _isSyncing.value = true

                // Step 1: Sync deletions first
                syncDeletions()

                // Step 2: Get server notes
                val result = noteService.getNoteResponses()
                result.onSuccess { serverNotes ->

                    // Step 3: Handle conflicts
                    resolveConflicts(serverNotes)

                    // Step 4: Sync local changes (create/update)
                    syncNotes()

                    // Step 5: Cleanup old deleted notes
                    noteDataSource.cleanupDeletedNotes()

                    Logger.d("Complete sync finished successfully",null,"Sync")

                }.onFailure { error ->
                    Logger.e( "SyncError",null) { "Failed to get server notes: $error" }
                }
            } catch (e: Exception) {
                Logger.e("SyncError", e) { "Unexpected error during sync" }
            } finally {
                _isSyncing.value = false
            }
        }
    }

    /**
     * Sync notes marked for deletion
     */
    private suspend fun syncDeletions() {
        val notesToDelete = noteDataSource.getNotesMarkedForDeletion()
        Logger.d("Syncing ${notesToDelete.size} deletions",null,"Sync")

        notesToDelete.forEach { note ->
            try {
                val result = noteService.deleteNote(note.noteId!!)
                result.onSuccess {
                    // Successfully deleted from server - remove completely
                    noteDataSource.deleteNoteById(note.id)
                    Logger.d("Successfully deleted note ${note.id} from server",null,"Sync")
                }.onFailure { error ->
                    Logger.e("DeleteSync", null) { "Failed to delete note ${note.id}: $error" }
                    // Keep the note marked for deletion - will retry next sync
                }
                delay(100) // Rate limiting
            } catch (e: Exception) {
                Logger.e("DeleteSync", e) { "Error deleting note ${note.id}" }
            }
        }
    }

    /**
     * Enhanced conflict resolution with multiple strategies
     */
    private suspend fun resolveConflicts(serverNoteResponses: List<NoteResponse>) {
        val localUnsyncedNotes = noteDataSource.getUnSyncedNotes()
        var conflictsFound = 0
        var autoResolved = 0
        val manualConflicts = mutableListOf<ConflictPair>()

        serverNoteResponses.forEach { serverNote ->
            val conflictingLocalNote = localUnsyncedNotes.find { it.id == serverNote.id }

            if (conflictingLocalNote != null) {
                conflictsFound++

                // Skip if local note is marked for deletion
                if (conflictingLocalNote.pendingDeletion || conflictingLocalNote.isDeleted) {
                    Logger.d("Local note ${serverNote.id} marked for deletion, skipping",null,"Conflict")
                    return@forEach
                }

                val resolution = resolveConflictStrategy(serverNote, conflictingLocalNote)

                when (resolution.strategy) {
                    ConflictStrategy.KEEP_SERVER -> {
                        noteDataSource.insertNote(serverNote.markAsSynced(), true)
                        autoResolved++
                        Logger.d( "Auto-resolved: kept server version for ${serverNote.id}",null,"Conflict")
                    }

                    ConflictStrategy.KEEP_LOCAL -> {
                        // Local will be synced to server in next step
                        autoResolved++
                        Logger.d("Auto-resolved: kept local version for ${serverNote.id}",null,"Conflict")
                    }

                    ConflictStrategy.AUTO_MERGE -> {
                        noteDataSource.insertNote(resolution.mergedNoteResponse!!.markForSync(), false)
                        autoResolved++
                        Logger.d( "Auto-merged note ${serverNote.id}")
                    }

                    ConflictStrategy.MANUAL -> {
                        manualConflicts.add(ConflictPair(serverNote, conflictingLocalNote))
                        Logger.d( "Manual resolution needed for ${serverNote.id}",null,"Conflict")
                    }
                }
            } else {
                // No local conflict - check if note was deleted locally
                val deletedNote = noteDataSource.getDeletedNote(serverNote.noteId!!)
                if (deletedNote == null) {
                    // Not deleted locally, safe to insert
                    noteDataSource.insertNote(serverNote.markAsSynced(), true)
                } else {
                    Logger.d("Server note ${serverNote.id} exists but was deleted locally",null,"Conflict")
                }
            }
        }

        // Update conflict state for UI
        if (manualConflicts.isNotEmpty()) {
            _conflictingNotes.value = manualConflicts
        }

        Logger.d("Conflicts found: $conflictsFound, Auto-resolved: $autoResolved, Manual: ${manualConflicts.size}",null,"Conflict")
    }

    /**
     * Determine conflict resolution strategy
     */
    private fun resolveConflictStrategy(serverNoteResponse: NoteResponse, localNoteResponse: NoteResponse): ConflictResolution {
        val serverTimestamp = serverNoteResponse.getUpdatedAtTimestamp()
        val localTimestamp = localNoteResponse.getUpdatedAtTimestamp()
        val timeDiff = abs(serverTimestamp - localTimestamp)

        return when {
            // If time difference is significant (> 5 minutes), use timestamp
            timeDiff > 5 * 60 * 1000 -> {
                if (serverTimestamp > localTimestamp) {
                    ConflictResolution(ConflictStrategy.KEEP_SERVER)
                } else {
                    ConflictResolution(ConflictStrategy.KEEP_LOCAL)
                }
            }

            // If times are close, try auto-merge
            canAutoMerge(serverNoteResponse, localNoteResponse) -> {
                val merged = performAutoMerge(serverNoteResponse, localNoteResponse)
                ConflictResolution(ConflictStrategy.AUTO_MERGE, mergedNoteResponse = merged)
            }

            // Otherwise, require manual resolution
            else -> ConflictResolution(ConflictStrategy.MANUAL, serverNoteResponse, localNoteResponse)
        }
    }

    /**
     * Check if notes can be automatically merged
     */
    private fun canAutoMerge(serverNoteResponse: NoteResponse, localNoteResponse: NoteResponse): Boolean {
        return when {
            // Only title changed
            serverNoteResponse.content == localNoteResponse.content -> true

            // Only content changed and no overlapping edits
            serverNoteResponse.title == localNoteResponse.title &&
                    !hasOverlappingEdits(serverNoteResponse.content, localNoteResponse.content) -> true

            // Same content, different metadata
            serverNoteResponse.content == localNoteResponse.content &&
                    serverNoteResponse.title == localNoteResponse.title -> true

            else -> false
        }
    }

    /**
     * Perform automatic merge of two notes
     */
    @OptIn(ExperimentalTime::class)
    private fun performAutoMerge(serverNoteResponse: NoteResponse, localNoteResponse: NoteResponse): NoteResponse {
        return when {
            serverNoteResponse.content == localNoteResponse.content -> {
                // Content same, merge titles
                val mergedTitle = if (serverNoteResponse.title.length > localNoteResponse.title.length) {
                    serverNoteResponse.title
                } else {
                    localNoteResponse.title
                }
                localNoteResponse.copy(
                    title = mergedTitle,
                    updatedAt = maxOf(serverNoteResponse.getUpdatedAtTimestamp(), localNoteResponse.getUpdatedAtTimestamp()).toString()
                )
            }

            serverNoteResponse.title == localNoteResponse.title -> {
                // Title same, append content
                val mergedContent = "${localNoteResponse.content}\n\n--- Merged from other device ---\n${serverNoteResponse.content}"
                localNoteResponse.copy(
                    content = mergedContent,
                    updatedAt = Clock.System.now().toString()
                )
            }

            else -> {
                // Fallback - prefer local with server metadata
                localNoteResponse.copy(
                    updatedAt = maxOf(serverNoteResponse.getUpdatedAtTimestamp(), localNoteResponse.getUpdatedAtTimestamp()).toString()
                )
            }
        }
    }

    /**
     * Check for overlapping edits in content
     */
    private fun hasOverlappingEdits(content1: String, content2: String): Boolean {
        val words1 = content1.split(Regex("\\s+")).filter { it.isNotBlank() }
        val words2 = content2.split(Regex("\\s+")).filter { it.isNotBlank() }

        if (words1.isEmpty() || words2.isEmpty()) return false

        val commonWords = words1.intersect(words2.toSet()).size
        val totalWords = maxOf(words1.size, words2.size)

        // If less than 70% words are common, consider as overlapping edits
        return commonWords.toDouble() / totalWords < 0.7
    }

    /**
     * Sync local changes to server
     */
    suspend fun syncNotes() = withContext(Dispatchers.IO) {
        val notes = noteDataSource.getUnSyncedNotes().filter {
            !it.isDeleted && !it.pendingDeletion
        }

        Logger.d("Syncing ${notes.size} local changes to server",null,"Sync")

        notes.forEach { note ->
            try {
                val req = NoteRequest(
                    id = note.noteId,
                    title = note.title,
                    content = note.content,
                    color = note.colorRes
                )
                noteService.upsert(req)
                    .onSuccess {
                        noteDataSource.insertNote(note.markAsSynced(), true)
                        Logger.d("Successfully synced note ${note.id}",null,"Sync")
                    }
                    .onFailure { error ->
                        Logger.e("Sync", null) { "Failed to sync note ${note.id}: $error" }
                    }
                delay(100) // Rate limiting
            } catch (e: Exception) {
                Logger.e("Sync", e) { "Error syncing note ${note.id}" }
            }
        }
    }

    /**
     * Manual conflict resolution by user
     */
    fun resolveConflictManually(conflictPair: ConflictPair, chooseServer: Boolean) {
        viewModelScope.launch {
            try {
                val noteToKeep = if (chooseServer) {
                    conflictPair.serverVersion.markAsSynced()
                } else {
                    conflictPair.localVersion.markForSync()
                }

                noteDataSource.insertNote(noteToKeep, chooseServer)

                // Remove from conflicts list
                val currentConflicts = _conflictingNotes.value.toMutableList()
                currentConflicts.remove(conflictPair)
                _conflictingNotes.value = currentConflicts

                Logger.d("Manually resolved conflict for ${conflictPair.serverVersion.id}",null,"Conflict")

            } catch (e: Exception) {
                Logger.e("Conflict", e) { "Error resolving conflict manually" }
            }
        }
    }

    /**
     * Check if device is online
     */
    private fun isOnline(): Boolean {
        return Konnection.instance.isConnected()
    }
}

// Data classes for conflict resolution
data class ConflictPair(
    val serverVersion: NoteResponse,
    val localVersion: NoteResponse
)

data class ConflictResolution(
    val strategy: ConflictStrategy,
    val serverNoteResponse: NoteResponse? = null,
    val localNoteResponse: NoteResponse? = null,
    val mergedNoteResponse: NoteResponse? = null
)

enum class ConflictStrategy {
    KEEP_SERVER,
    KEEP_LOCAL,
    AUTO_MERGE,
    MANUAL
}