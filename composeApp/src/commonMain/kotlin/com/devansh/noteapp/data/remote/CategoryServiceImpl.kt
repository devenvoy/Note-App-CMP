package com.devansh.noteapp.data.remote

import com.devansh.noteapp.BuildConfig
import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.model.Category
import com.devansh.noteapp.domain.repo.CategoryService
import com.devansh.noteapp.domain.utils.BaseGateway
import com.devansh.noteapp.domain.utils.Result
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class CategoryServiceImpl(httpClient: HttpClient) : CategoryService, BaseGateway(httpClient) {

    private val categoryApi = BuildConfig.BASE_URL + "/categories"

    override suspend fun getCategories(accessToken: String): Result<List<Category>, ServerError> {
        return tryToExecute<List<Category>> {
            get(categoryApi) {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }
    }

    override suspend fun createCategory(
        category: Category, accessToken: String
    ): Result<Category, ServerError> {
        return tryToExecute<Category> {
            post(categoryApi) {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                setBody(category)
            }
        }
    }

    override suspend fun updateCategory(
        category: Category,
        accessToken: String
    ): Result<Category, ServerError> {
        return tryToExecute<Category> {
            put("$categoryApi/${category.id}") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                setBody(category)
            }
        }
    }

    override suspend fun deleteCategory(id: String, accessToken: String) {
        try {
            executeOrThrow<Unit> {
                delete("$categoryApi/$id") {
                    header(HttpHeaders.Authorization, "Bearer $accessToken")
                }
            }
        } catch (e: Exception) {
            Logger.SIMPLE.log("network ${e.message}")
        }
    }
}