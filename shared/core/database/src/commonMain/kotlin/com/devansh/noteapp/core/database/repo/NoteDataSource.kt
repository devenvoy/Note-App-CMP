package com.devansh.noteapp.core.database.repo

import com.devansh.noteapp.NoteEntity
import com.devansh.noteapp.data.models.dto.NoteResponse
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {
    suspend fun inTx(block: suspend () -> Unit)
    suspend fun getAllNotes(): Flow<List<NoteResponse>>
    suspend fun getNoteById(id: Long): NoteResponse?
    suspend fun getNoteByNoteId(noteId: String): NoteResponse?
    suspend fun insertNote(NoteResponse: NoteResponse,synced:Boolean)
    suspend fun deleteNoteById(id: Long)
    suspend fun deleteNoteById(noteId: String)
    suspend fun getUnSyncedNotes(): List<NoteResponse>
    suspend fun getSyncedNotes() : List<NoteResponse>
    suspend fun markNoteAsSynced(id: Long)
    suspend fun emptyNoteTable()
    suspend fun markAsDeleted(noteId: String)
    suspend fun markForDeletion(noteId: String)
    suspend fun getNotesMarkedForDeletion(): List<NoteResponse>
    suspend fun getDeletedNote(noteId: String): NoteResponse?
    suspend fun cleanupDeletedNotes()


    fun NoteEntity.toNote() = NoteResponse(
        id = id,
        noteId = note_id,
        title = title,
        content = content,
        colorRes = colorRes,
        category = categoryId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}