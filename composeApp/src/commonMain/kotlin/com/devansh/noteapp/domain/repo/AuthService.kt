package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.entity.ServerResponse
import com.devansh.noteapp.domain.utils.Result
import com.devansh.noteapp.domain.model.AuthResponse

interface AuthService {
    suspend fun login(email: String, password: String): Result<AuthResponse, ServerError>
    suspend fun register(email: String, password: String): Result<Unit, ServerError>
    suspend fun refreshAuth(refreshToken: String): Result<AuthResponse, ServerError>
}