package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.model.Note

interface NoteDataSource {
    suspend fun getAllNotes(): List<Note>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note)
    suspend fun deleteNoteById(id: Long)
}