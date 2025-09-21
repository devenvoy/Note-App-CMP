package com.devansh.noteapp.core.database.repo

import com.devansh.noteapp.CategoryEntity
import com.devansh.noteapp.data.models.dto.Category
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
    fun CategoryEntity.toCategory() =
        Category(id = categoryId, name = categoryName, color = colorRes, isSynced = isSynced == 1L)
}