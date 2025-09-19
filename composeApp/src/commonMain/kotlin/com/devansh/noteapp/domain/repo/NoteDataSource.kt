package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {
    suspend fun inTx(block: suspend () -> Unit)
    suspend fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: String): Note?
    suspend fun insertNote(note: Note,synced:Boolean)
    suspend fun deleteNoteById(id: String)
    suspend fun getUnSyncedNotes(): List<Note>
    suspend fun getSyncedNotes() : List<Note>
    suspend fun markNoteAsSynced(id: String)
    suspend fun emptyNoteTable()
    suspend fun markAsDeleted(id: String)
    suspend fun markForDeletion(id: String)
    suspend fun getNotesMarkedForDeletion(): List<Note>
    suspend fun getDeletedNote(id: String): Note?
    suspend fun cleanupDeletedNotes()
}