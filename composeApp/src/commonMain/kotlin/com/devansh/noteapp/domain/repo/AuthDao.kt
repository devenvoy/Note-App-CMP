package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.data.entity.ServerError
import com.devansh.noteapp.data.entity.ServerResponse
import com.devansh.noteapp.data.remote.utils.Result
import com.devansh.noteapp.domain.model.AuthResponse

interface AuthDao {
    suspend fun login(email: String, password: String): Result<ServerResponse<AuthResponse>, ServerError>
    suspend fun register(email: String, password: String): Result<ServerResponse<AuthResponse>, ServerError>
}