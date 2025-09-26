package com.devansh.noteapp.core.database.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.devansh.noteapp.core.database.DbHelper
import com.devansh.noteapp.core.database.repo.NoteDataSource
import com.devansh.noteapp.core.utils.IO
import com.devansh.noteapp.data.models.dto.NoteResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NoteDataSourceImpl(
    private val dbHelper: DbHelper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : NoteDataSource {

    override suspend fun inTx(block: suspend () -> Unit) {
    }

    override suspend fun getAllNotes(): Flow<List<NoteResponse>> = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.getAllNotes()
                .asFlow()
                .mapToList(dispatcher)
                .map { list -> list.map { entity -> entity.toNote() } }
        }
    }

    override suspend fun getNoteById(id: Long): NoteResponse? = withContext(dispatcher) {
        dbHelper.withDatabaseOrNull { database ->
            database.noteDatabaseQueries.getNoteById(id).executeAsOneOrNull()?.toNote()
        }
    }

    override suspend fun getNoteByNoteId(noteId: String): NoteResponse? = withContext(dispatcher) {
        dbHelper.withDatabaseOrNull { database ->
            database.noteDatabaseQueries.getNoteByNoteId(noteId)
                .executeAsOneOrNull()?.toNote()
        }
    }

    override suspend fun insertNote(note: NoteResponse, synced: Boolean): Long =
        withContext(dispatcher) {
            dbHelper.withDatabase { database ->
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
        }

    override suspend fun deleteNoteById(id: Long) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.deleteNoteById(id = id)
            Unit
        }
    }

    override suspend fun markAsDeleted(id: Long) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.markAsDeletedById(id)
            Unit
        }
    }

    override suspend fun markForDeletion(id: Long) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.markForDeletionById(id)
            Unit
        }
    }

    // DELETION METHODS BY SERVER NOTE ID
    override suspend fun deleteNoteByNoteId(noteId: String) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.deleteNoteByNoteId(noteId)
            Unit
        }
    }

    override suspend fun markAsDeletedByNoteId(noteId: String) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.markAsDeletedByNoteId(noteId)
            Unit
        }
    }

    override suspend fun markForDeletionByNoteId(noteId: String) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.markForDeletionByNoteId(noteId)
            Unit
        }
    }

    // SYNC RELATED METHODS
    override suspend fun getUnSyncedNotes(): List<NoteResponse> = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.getAllUnsyncedNotes()
                .executeAsList()
                .map { it.toNote() }
        }
    }

    override suspend fun getSyncedNotes(): List<NoteResponse> = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.getAllSyncedNotes()
                .executeAsList()
                .map { it.toNote() }
        }
    }

    override suspend fun markNoteAsSynced(id: Long) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.markNoteAsSynced(id)
            Unit
        }
    }

    override suspend fun getNotesMarkedForDeletion(): List<NoteResponse> = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.getNotesMarkedForDeletion()
                .executeAsList()
                .map { it.toNote() }
        }
    }

    override suspend fun getDeletedNote(noteId: String): NoteResponse? = withContext(dispatcher) {
        dbHelper.withDatabaseOrNull { database ->
            database.noteDatabaseQueries.getDeletedNote(noteId)
                .executeAsOneOrNull()
                ?.toNote()
        }
    }

    // UTILITY METHODS
    override suspend fun emptyNoteTable() = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.emptyNoteTable()
            Unit
        }
    }

    /**
     * Permanently delete notes that have been successfully synced for deletion
     */
    override suspend fun cleanupDeletedNotes() = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.transaction {
                // Delete notes that are marked as deleted AND not pending deletion
                database.noteDatabaseQueries.cleanupDeletedNotes()
            }
        }
    }

    // ADDITIONAL HELPER METHODS (using your existing queries)

    /**
     * Soft delete - marks note as deleted but keeps it for sync
     */
    suspend fun softDeleteNote(noteId: String) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.softDeleteNote(noteId)
            Unit
        }
    }

    /**
     * Restore a soft-deleted note
     */
    suspend fun restoreNote(id: Long) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.restoreNote(id)
            Unit
        }
    }

    /**
     * Get all deleted notes (for debugging/admin purposes)
     */
    suspend fun getAllDeletedNotes(): List<NoteResponse> = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.noteDatabaseQueries.getAllDeletedNotes()
                .executeAsList()
                .map { it.toNote() }
        }
    }
}