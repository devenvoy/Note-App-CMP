package com.devansh.noteapp.data.local

import com.devansh.noteapp.NoteDatabase
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.model.toNote
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.utils.DateTimeUtil

class SqlDelightNoteDataSource(db: NoteDatabase) : NoteDataSource {

    private val queries = db.noteDatabaseQueries

    override suspend fun getAllNotes(): List<Note> {
        return queries.getAllNotes()
            .executeAsList()
            .map { it.toNote() }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return queries.getNoteById(id = id)
            .executeAsOneOrNull()
            ?.toNote()
    }

    override suspend fun insertNote(note: Note) {
        queries.insertNote(
            id = note.id,
            title = note.title,
            content = note.content,
            colorres = note.colorRes,
            created = DateTimeUtil.toEpochMillis(note.created)
        )
    }

    override suspend fun deleteNoteById(id: Long) {
        queries.deleteNoteById(id = id)
    }
}