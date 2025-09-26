package com.devansh.noteapp.core.database.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.devansh.noteapp.core.database.DbHelper
import com.devansh.noteapp.core.database.repo.CategoryDataSource
import com.devansh.noteapp.core.utils.IO
import com.devansh.noteapp.data.models.dto.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CategoryDataSourceImpl(
    private val dbHelper: DbHelper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CategoryDataSource {

    override suspend fun inTx(block: suspend () -> Unit) {
    }

    override suspend fun getAllCategories(): Flow<List<Category>> = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
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
                        ).apply { updateNoteCount(entity.notesCount) }
                    }
                }
        }
    }

    override suspend fun getCategoryById(id: String): Category? = withContext(dispatcher) {
        dbHelper.withDatabaseOrNull { database ->
            database.categoryDatabaseQueries.getCategoryById(categoryId = id)
                .executeAsOneOrNull()
                ?.toCategory()
        }
    }

    override suspend fun insertCategory(
        category: Category,
        synced: Boolean
    ) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.categoryDatabaseQueries.insertCategory(
                categoryId = category.id,
                colorRes = category.color,
                categoryName = category.name,
                isSynced = if (synced) 1 else 0,
                createdAt = category.createdAt,
                updatedAt = category.updatedAt
            )
            Unit
        }
    }

    override suspend fun deleteCategoryById(id: String) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.categoryDatabaseQueries.deleteCategotyById(categoryId = id)
            Unit
        }
    }

    override suspend fun getUnSyncedCategories(): List<Category> = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.categoryDatabaseQueries.getAllUnsyncedCategories()
                .executeAsList()
                .map { it.toCategory() }
        }
    }

    override suspend fun getSyncedCategories(): List<Category> = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.categoryDatabaseQueries.getAllSyncedCategories()
                .executeAsList()
                .map { it.toCategory() }
        }
    }

    override suspend fun markCategoryAsSynced(id: String) = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.categoryDatabaseQueries.markCatgoryAsSynced(id)
            Unit
        }
    }

    override suspend fun emptyCategoryTable() = withContext(dispatcher) {
        dbHelper.withDatabase { database ->
            database.categoryDatabaseQueries.emptyCategoryTable()
            Unit
        }
    }
}