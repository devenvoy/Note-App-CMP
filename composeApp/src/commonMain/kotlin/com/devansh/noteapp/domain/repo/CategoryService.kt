package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.model.Category
import com.devansh.noteapp.domain.utils.Result

interface CategoryService {
    suspend fun getCategories(accessToken: String): Result<List<Category>, ServerError>
    suspend fun createCategory(name: String, accessToken: String): Result<Category, ServerError>
    suspend fun updateCategory(id: String, name: String, accessToken: String): Result<Category, ServerError>
    suspend fun deleteCategory(id: String, accessToken: String)
}