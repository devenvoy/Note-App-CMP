package com.devansh.noteapp.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.devansh.noteapp.NoteDatabase
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.model.toNote
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.utils.DateTimeUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteDataSourceImpl(
    db: NoteDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : NoteDataSource {

    private val queries = db.noteDatabaseQueries

    override suspend fun getAllNotes(): Flow<List<Note>> {
        return queries.getAllNotes()
            .asFlow()
            .mapToList(dispatcher)
            .map { list ->
                list.map { entity ->
                    entity.toNote()
                }
            }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return queries.getNoteById(id = id)
            .executeAsOneOrNull()
            ?.toNote()
    }

    override suspend fun insertNote(note: Note,synced:Boolean) {
        queries.insertNote(
            id = note.id,
            title = note.title,
            content = note.content,
            colorres = note.colorRes,
            category = note.category,
            last_modified = DateTimeUtil.toEpochMillis(note.lastModified),
            isSynced = if(synced) 1 else 0
        )
    }

    override suspend fun deleteNoteById(id: Long) {
        queries.deleteNoteById(id = id)
    }

    override suspend fun getUnSyncedNotes(): List<Note> {
        return queries.getAllUnsyncedNotes()
            .executeAsList()
            .map { it.toNote() }

    }

    override suspend fun getSyncedNotes(): List<Note> {
        return queries.getAllSyncedNotes()
            .executeAsList()
            .map { it.toNote() }
    }

    override suspend fun markNoteAsSynced(id: Long) {
        queries.markNoteAsSync(id)
    }
}