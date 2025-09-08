package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryDataSource {
    suspend fun inTx(block: suspend () -> Unit)
    suspend fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: String): Category?
    suspend fun insertCategory(category: Category, synced: Boolean)
    suspend fun deleteCategoryById(id: String)
    suspend fun getUnSyncedCategories(): List<Category>
    suspend fun getSyncedCategories(): List<Category>
    suspend fun markCategoryAsSynced(id: String)
    suspend fun emptyCategoryTable()
}