package com.devansh.noteapp.core.database.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import com.devansh.noteapp.core.database.NoteAppDatabase
import com.devansh.noteapp.core.database.repo.NoteDataSource
import com.devansh.noteapp.data.models.dto.NoteResponse
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

    override suspend fun getAllNotes(): Flow<List<NoteResponse>> = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getAllNotes()
            .asFlow()
            .mapToList(dispatcher)
            .map { list -> list.map { entity -> entity.toNote() } }
    }

    override suspend fun getNoteById(id: Long): NoteResponse? = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getNoteById(id)
            .executeAsOneOrNull()
            ?.toNote()
    }

    override suspend fun getNoteByNoteId(noteId: String): NoteResponse? = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getNoteByNoteId(noteId)
            .executeAsOneOrNull()
            ?.toNote()
    }

    override suspend fun insertNote(note: NoteResponse, synced: Boolean):Long = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.insertNote(
            id = if (note.id == -1L) null else note.id, // Handle auto-increment
            note_id = note.noteId,
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
        database.noteDatabaseQueries.lastInsertRowId().executeAsOne()
    }

    override suspend fun deleteNoteById(id: Long) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.deleteNoteById(id = id)
        Unit
    }

    override suspend fun markAsDeleted(id: Long) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.markAsDeletedById(id)
        Unit
    }

    override suspend fun markForDeletion(id: Long) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.markForDeletionById(id)
        Unit
    }

    // DELETION METHODS BY SERVER NOTE ID
    override suspend fun deleteNoteByNoteId(noteId: String) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.deleteNoteByNoteId(noteId)
        Unit
    }

    override suspend fun markAsDeletedByNoteId(noteId: String) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.markAsDeletedByNoteId(noteId)
        Unit
    }

    override suspend fun markForDeletionByNoteId(noteId: String) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.markForDeletionByNoteId(noteId)
        Unit
    }

    // SYNC RELATED METHODS
    override suspend fun getUnSyncedNotes(): List<NoteResponse> = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getAllUnsyncedNotes()
            .executeAsList()
            .map { it.toNote() }
    }

    override suspend fun getSyncedNotes(): List<NoteResponse> = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getAllSyncedNotes()
            .executeAsList()
            .map { it.toNote() }
    }

    override suspend fun markNoteAsSynced(id: Long) = withContext(dispatcher) {
        db.first().noteDatabaseQueries.markNoteAsSynced(id)
        Unit
    }

    override suspend fun getNotesMarkedForDeletion(): List<NoteResponse> = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getNotesMarkedForDeletion()
            .executeAsList()
            .map { it.toNote() }
    }

    override suspend fun getDeletedNote(noteId: String): NoteResponse? = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getDeletedNote(noteId)
            .executeAsOneOrNull()
            ?.toNote()
    }

    // UTILITY METHODS
    override suspend fun emptyNoteTable() = withContext(dispatcher) {
        db.first().noteDatabaseQueries.emptyNoteTable()
        Unit
    }

    /**
     * Permanently delete notes that have been successfully synced for deletion
     */
    override suspend fun cleanupDeletedNotes() = withContext(dispatcher) {
        val database = db.first()
        database.transaction {
            // Delete notes that are marked as deleted AND not pending deletion
            database.noteDatabaseQueries.cleanupDeletedNotes()
        }
        Unit
    }

    // ADDITIONAL HELPER METHODS (using your existing queries)

    /**
     * Soft delete - marks note as deleted but keeps it for sync
     */
    suspend fun softDeleteNote(noteId: String) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.softDeleteNote(noteId)
        Unit
    }

    /**
     * Restore a soft-deleted note
     */
    suspend fun restoreNote(id: Long) = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.restoreNote(id)
        Unit
    }

    /**
     * Get all deleted notes (for debugging/admin purposes)
     */
    suspend fun getAllDeletedNotes(): List<NoteResponse> = withContext(dispatcher) {
        val database = db.first()
        database.noteDatabaseQueries.getAllDeletedNotes()
            .executeAsList()
            .map { it.toNote() }
    }
}