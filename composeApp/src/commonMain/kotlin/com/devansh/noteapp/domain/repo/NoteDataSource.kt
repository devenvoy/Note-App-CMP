package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {
    suspend fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note,synced:Boolean)
    suspend fun deleteNoteById(id: Long)
    suspend fun getUnSyncedNotes(): List<Note>
    suspend fun getSyncedNotes() : List<Note>
    suspend fun markNoteAsSynced(id: Long)
    suspend fun emptyNoteTable()
}