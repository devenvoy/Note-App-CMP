package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.entity.ServerResponse
import com.devansh.noteapp.domain.utils.Result
import com.devansh.noteapp.domain.model.AuthResponse

interface AuthDao {
    suspend fun login(email: String, password: String): Result<ServerResponse<AuthResponse>, ServerError>
    suspend fun register(email: String, password: String): Result<ServerResponse<AuthResponse>, ServerError>
}