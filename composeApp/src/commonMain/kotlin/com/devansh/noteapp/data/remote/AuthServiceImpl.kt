package com.devansh.noteapp.data.remote

import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.model.AuthResponse
import com.devansh.noteapp.domain.repo.AuthService
import com.devansh.noteapp.domain.utils.BaseGateway
import com.devansh.noteapp.domain.utils.Result
import com.jignesh.society.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthServiceImpl(private val httpClient: HttpClient) : AuthService, BaseGateway(httpClient) {

    private val login = "/auth/login"
    private val register = "/auth/register"
    private val refresh = "/auth/refresh"

    override suspend fun login(email: String, password: String): Result<AuthResponse, ServerError> {
        return tryToExecute<AuthResponse> {
            post(BuildConfig.BASE_URL + login) {
                contentType(ContentType.Application.Json)
                setBody("""{"email": "$email","password": "$password"}""".trimIndent())
            }
        }
    }

    override suspend fun register(email: String, password: String): Result<Unit, ServerError> {
        return tryToExecute<Unit> {
            post(BuildConfig.BASE_URL + register) {
                contentType(ContentType.Application.Json)
                setBody("""{"email": "$email","password": "$password"}""".trimIndent())
            }
        }
    }

    override suspend fun refreshAuth(refreshToken: String): Result<AuthResponse, ServerError> {
        return tryToExecute {
            post(BuildConfig.BASE_URL + refresh) {
                contentType(ContentType.Application.Json)
                setBody("""{"refreshToken": "$refreshToken"}""".trimIndent())
            }
        }
    }
}