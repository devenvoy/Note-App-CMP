package com.devansh.noteapp.core.database.repo

import com.devansh.noteapp.NoteEntity
import com.devansh.noteapp.data.models.dto.NoteResponse
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {
    suspend fun inTx(block: suspend () -> Unit)
    suspend fun getAllNotes(): Flow<List<NoteResponse>>
    suspend fun getNoteById(id: Long): NoteResponse?
    suspend fun getNoteByNoteId(noteId: String): NoteResponse?
    suspend fun insertNote(note: NoteResponse, synced: Boolean): Long

    // DELETION METHODS BY LOCAL ID
    suspend fun deleteNoteById(id: Long)
    suspend fun markAsDeleted(id: Long)
    suspend fun markForDeletion(id: Long)

    // DELETION METHODS BY SERVER NOTE ID
    suspend fun deleteNoteByNoteId(noteId: String)
    suspend fun markAsDeletedByNoteId(noteId: String)
    suspend fun markForDeletionByNoteId(noteId: String)

    // SYNC RELATED METHODS
    suspend fun getUnSyncedNotes(): List<NoteResponse>
    suspend fun getSyncedNotes(): List<NoteResponse>
    suspend fun markNoteAsSynced(id: Long)
    suspend fun getNotesMarkedForDeletion(): List<NoteResponse>
    suspend fun getDeletedNote(noteId: String): NoteResponse?
    suspend fun cleanupDeletedNotes()

    // UTILITY METHODS
    suspend fun emptyNoteTable()

    fun NoteEntity.toNote() = NoteResponse(
        id = id,
        noteId = note_id,
        title = title,
        content = content,
        colorRes = colorRes,
        category = categoryId,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = isSynced == 1L,
        isDeleted = isDeleted == 1L,
        pendingDeletion = pendingDeletion == 1L
    )
}