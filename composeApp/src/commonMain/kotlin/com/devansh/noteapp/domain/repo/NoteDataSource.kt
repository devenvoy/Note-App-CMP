package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {
    suspend fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note)
    suspend fun deleteNoteById(id: Long)
}