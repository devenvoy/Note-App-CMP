package com.devansh.noteapp.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import com.devansh.noteapp.NoteAppDatabase
import com.devansh.noteapp.domain.model.Category
import com.devansh.noteapp.domain.model.toCategory
import com.devansh.noteapp.domain.repo.CategoryDataSource
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
import kotlinx.coroutines.withContext

class CategoryDataSourceImpl(
    private val sqlDriver: SqlDriver,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CategoryDataSource {

    private val db = flow {
        NoteAppDatabase.Schema.create(sqlDriver).await()
        val database = NoteAppDatabase.invoke(sqlDriver)
        emit(database)
    }.shareIn(GlobalScope, SharingStarted.Lazily, 1)

    override suspend fun inTx(block: suspend () -> Unit) {
        db.first().transaction { block() }
    }

    override suspend fun getAllCategories(): Flow<List<Category>> = withContext(dispatcher) {
        val database = db.first()
        database.categoryDatabaseQueries.getAllCategoriesWithNotesCount()
            .asFlow()
            .mapToList(dispatcher)
            .map { list ->
                list.map { entity ->
                    Category(
                        id = entity.categoryId,
                        name = entity.categoryName,
                        color = entity.colorRes,
                        isSynced = entity.isSynced == 1L,
                        notesCount = entity.notesCount,
                    )
                }
            }
    }

    override suspend fun getCategoryById(id: String): Category? = withContext(dispatcher) {
        val database = db.first()
        database.categoryDatabaseQueries.getCategoryById(categoryId = id)
            .executeAsOneOrNull()
            ?.toCategory()
    }

    override suspend fun insertCategory(
        category: Category,
        synced: Boolean
    ) = withContext(dispatcher) {
        val database = db.first()
        database.categoryDatabaseQueries.insertCategory(
            categoryId = category.id.toString(),
            colorRes = category.color,
            categoryName = category.name,
            isSynced = if (synced) 1 else 0,
            createdAt = category.createdAt,
            updatedAt = category.updatedAt
        )
        Unit
    }

    override suspend fun deleteCategoryById(id: String) = withContext(dispatcher) {
        val database = db.first()
        database.categoryDatabaseQueries.deleteCategotyById(categoryId = id)
        Unit
    }

    override suspend fun getUnSyncedCategories(): List<Category> = withContext(dispatcher) {
        val database = db.first()
        database.categoryDatabaseQueries.getAllUnsyncedCategories()
            .executeAsList()
            .map { it.toCategory() }
    }

    override suspend fun getSyncedCategories(): List<Category> = withContext(dispatcher) {
        val database = db.first()
        database.categoryDatabaseQueries.getAllSyncedCategories()
            .executeAsList()
            .map { it.toCategory() }
    }

    override suspend fun markCategoryAsSynced(id: String) = withContext(dispatcher) {
        db.first().categoryDatabaseQueries.markCatgoryAsSynced(id)
        Unit
    }

    override suspend fun emptyCategoryTable() = withContext(dispatcher) {
        db.first().categoryDatabaseQueries.emptyCategoryTable()
        Unit
    }
}