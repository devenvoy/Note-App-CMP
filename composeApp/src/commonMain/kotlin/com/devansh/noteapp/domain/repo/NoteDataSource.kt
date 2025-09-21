package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {
    suspend fun inTx(block: suspend () -> Unit)
    suspend fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(noteId: String): Note?
    suspend fun insertNote(note: Note,synced:Boolean)
    suspend fun deleteNoteById(id: Long)
    suspend fun deleteNoteById(noteId: String)
    suspend fun getUnSyncedNotes(): List<Note>
    suspend fun getSyncedNotes() : List<Note>
    suspend fun markNoteAsSynced(id: Long)
    suspend fun emptyNoteTable()
    suspend fun markAsDeleted(noteId: String)
    suspend fun markForDeletion(noteId: String)
    suspend fun getNotesMarkedForDeletion(): List<Note>
    suspend fun getDeletedNote(noteId: String): Note?
    suspend fun cleanupDeletedNotes()
}