package com.devansh.noteapp.data.repository.impl

import com.devansh.noteapp.core.entity.ServerError
import com.devansh.noteapp.core.network.BaseGateway
import com.devansh.noteapp.core.utils.Result
import com.devansh.noteapp.data.models.dto.Category
import com.devansh.noteapp.data.repository.repo.CategoryService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class CategoryServiceImpl(httpClient: HttpClient) : CategoryService, BaseGateway(httpClient) {

    private val categoryApi = "/categories"

    override suspend fun getCategories(): Result<List<Category>, ServerError> {
        return tryToExecute<List<Category>> {
            get(categoryApi)
        }
    }

    override suspend fun createCategory(category: Category): Result<Category, ServerError> {
        return tryToExecute<Category> {
            post(categoryApi) { setBody(category) }
        }
    }

    override suspend fun updateCategory(category: Category): Result<Category, ServerError> {
        return tryToExecute<Category> {
            put("$categoryApi/${category.id}") { setBody(category) }
        }
    }

    override suspend fun deleteCategory(id: String) {
        try {
            executeOrThrow<Unit> { delete("$categoryApi/$id") }
        } catch (e: Exception) {
            Logger.SIMPLE.log("network ${e.message}")
        }
    }
}