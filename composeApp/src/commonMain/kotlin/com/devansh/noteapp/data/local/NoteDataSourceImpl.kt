package com.devansh.noteapp.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import com.devansh.noteapp.NoteDatabase
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.model.toNote
import com.devansh.noteapp.domain.repo.NoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class NoteDataSourceImpl(
    private val sqlDriver: SqlDriver,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : NoteDataSource {


    private val db = flow {
        NoteDatabase.Schema.create(sqlDriver).await()
        val database = NoteDatabase.invoke(sqlDriver)
        emit(database)
    }.shareIn(GlobalScope, SharingStarted.Lazily, 1)

    override suspend fun inTx(block: suspend () -> Unit) {
        db.first().transaction { block() }
    }

    override suspend fun getAllNotes(): Flow<List<Note>> {
        val database = db.first()
        return database.noteDatabaseQueries.getAllNotes()
            .asFlow()
            .mapToList(dispatcher)
            .map { list ->
                list.map { entity ->
                    entity.toNote()
                }
            }
    }

    override suspend fun getNoteById(id: String): Note? {
        val database = db.first()
        return database.noteDatabaseQueries.getNoteById(id = id)
            .executeAsOneOrNull()
            ?.toNote()
    }

    override suspend fun insertNote(note: Note, synced: Boolean) {
        val database = db.first()
        database.noteDatabaseQueries.insertNote(
            id = note.id.toString(),
            title = note.title,
            content = note.content,
            ownerId = note.ownerId,
            colorRes = note.colorRes,
            categoryId = note.category,
            categoryName = note.categoryName,
            isSynced = if (synced) 1 else 0,
        )
    }

    override suspend fun deleteNoteById(id: String) {
        val database = db.first()
        database.noteDatabaseQueries.deleteNoteById(id = id)
    }

    override suspend fun getUnSyncedNotes(): List<Note> {
        val database = db.first()
        return database.noteDatabaseQueries.getAllUnsyncedNotes()
            .executeAsList()
            .map { it.toNote() }

    }

    override suspend fun getSyncedNotes(): List<Note> {
        val database = db.first()
        return database.noteDatabaseQueries.getAllSyncedNotes()
            .executeAsList()
            .map { it.toNote() }
    }

    override suspend fun markNoteAsSynced(id: String) {
        db.first().noteDatabaseQueries.markNoteAsSynced(id)
    }

    override suspend fun emptyNoteTable() {
       db.first().noteDatabaseQueries.emptyNoteTable()
    }
}