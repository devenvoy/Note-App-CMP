package com.devansh.noteapp.data.remote

import com.devansh.noteapp.data.entity.ServerError
import com.devansh.noteapp.data.entity.ServerResponse
import com.devansh.noteapp.data.remote.utils.BaseGateway
import com.devansh.noteapp.data.remote.utils.Result
import com.devansh.noteapp.domain.model.AuthResponse
import com.devansh.noteapp.domain.repo.AuthDao
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthDaoImpl(
    private val httpClient: HttpClient
) : AuthDao , BaseGateway(httpClient) {
    override suspend fun login(email: String, password: String): Result<ServerResponse<AuthResponse>, ServerError> {
        val result = tryToExecute<ServerResponse<AuthResponse>> {
            post(""){
                contentType(ContentType.Application.Json)
                setBody("""
                    {
                    "email": "$email",
                    "password": "$password"
                    }
                """.trimIndent())
            }
        }
         return result
    }

    override suspend fun register(email: String, password: String): Result<ServerResponse<AuthResponse>, ServerError> {
        val result = tryToExecute<ServerResponse<AuthResponse>> {
            post(""){
                contentType(ContentType.Application.Json)
                setBody("""
                    {
                    "email": "$email",
                    "password": "$password"
                    }
                """.trimIndent())
            }
        }
        return result
    }
}