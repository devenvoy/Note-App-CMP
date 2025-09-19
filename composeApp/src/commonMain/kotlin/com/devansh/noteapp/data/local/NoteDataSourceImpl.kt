package com.devansh.noteapp.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import com.devansh.noteapp.NoteAppDatabase
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.model.toNote
import com.devansh.noteapp.domain.repo.NoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext

class NoteDataSourceImpl(
    private val sqlDriver: SqlDriver,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : NoteDataSource {

    @OptIn(DelicateCoroutinesApi::class)
    private val db = flow {
        NoteAppDatabase.Schema.create(sqlDriver).await()
        val database = NoteAppDatabase.invoke(sqlDriver)
        emit(database)
    }.shareIn(GlobalScope, SharingStarted.Lazily, 1)

    override suspend fun inTx(block: suspend () -> Unit) {
        db.first().transaction { block() }
    }

    override suspend fun getAllNotes(): Flow<List<Note>> = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getAllNotes()
            .asFlow()
            .mapToList(dispatcher)
            .map { list -> list.map { entity -> entity.toNote() } }
    }

    override suspend fun getNoteById(id: String): Note? = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getNoteById(id = id)
            .executeAsOneOrNull()
            ?.toNote()
    }

    override suspend fun insertNote(note: Note, synced: Boolean) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.insertNote(
            id = note.id.toString(),
            title = note.title,
            content = note.content,
            colorRes = note.colorRes,
            categoryId = note.category,
            isSynced = if (synced) 1L else 0L,
            isDeleted = if (note.isDeleted) 1L else 0L,
            pendingDeletion = if (note.pendingDeletion) 1L else 0L,
            createdAt = note.createdAt,
            updatedAt = note.updatedAt
        )
        Unit
    }

    override suspend fun deleteNoteById(id: String) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.deleteNoteById(id = id)
        Unit
    }

    override suspend fun getUnSyncedNotes(): List<Note> = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getAllUnsyncedNotes()
            .executeAsList()
            .map { it.toNote() }
    }

    override suspend fun getSyncedNotes(): List<Note> = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getAllSyncedNotes()
            .executeAsList()
            .map { it.toNote() }
    }

    override suspend fun markNoteAsSynced(id: String) = withContext(dispatcher) {
        db.first().noteDatabaseQueries.markNoteAsSynced(id)
        Unit
    }

    override suspend fun emptyNoteTable() = withContext(dispatcher) {
        db.first().noteDatabaseQueries.emptyNoteTable()
        Unit
    }

    // DELETION HANDLING METHODS

    override suspend fun markAsDeleted(id: String) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.markAsDeleted(id)
        Unit
    }

    override suspend fun markForDeletion(id: String) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.markForDeletion(id)
        Unit
    }

    override suspend fun getNotesMarkedForDeletion(): List<Note> = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getNotesMarkedForDeletion()
            .executeAsList()
            .map { it.toNote() }
    }

    override suspend fun getDeletedNote(id: String): Note? = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getDeletedNote(id)
            .executeAsOneOrNull()
            ?.toNote()
    }

    // ADDITIONAL HELPER METHODS

    /**
     * Soft delete - marks note as deleted but keeps it for sync
     */
    suspend fun softDeleteNote(id: String) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.softDeleteNote(id)
        Unit
    }

    /**
     * Restore a soft-deleted note
     */
    suspend fun restoreNote(id: String) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.restoreNote(id)
        Unit
    }

    /**
     * Get all deleted notes (for debugging/admin purposes)
     */
    suspend fun getAllDeletedNotes(): List<Note> = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getAllDeletedNotes()
            .executeAsList()
            .map { it.toNote() }
    }

    /**
     * Permanently delete notes that have been successfully synced for deletion
     */
    override suspend fun cleanupDeletedNotes() = withContext(dispatcher) {
      /*  val database = db.first()
        database.transaction {
            // Only delete notes that are marked as deleted AND not pending deletion
            // This means they've been successfully deleted from server
            database.noteDatabaseQueries.transaction {

                execute(
                    sql = "DELETE FROM noteEntity WHERE isDeleted = 1 AND pendingDeletion = 0",
                    parameters = 0,
                    binders = { }
                )
            }
        }*/
        Unit
    }
}