package com.devansh.noteapp.data.repository.repo

import com.devansh.noteapp.core.entity.ServerError
import com.devansh.noteapp.core.utils.Result
import com.devansh.noteapp.data.models.dto.Category

interface CategoryService {
    suspend fun getCategories(): Result<List<Category>, ServerError>
    suspend fun createCategory(category: Category): Result<Category, ServerError>
    suspend fun updateCategory(category: Category): Result<Category, ServerError>
    suspend fun deleteCategory(id: String)
}