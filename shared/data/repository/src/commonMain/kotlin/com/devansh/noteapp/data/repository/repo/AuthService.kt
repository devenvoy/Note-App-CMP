package com.devansh.noteapp.data.repository.repo

import com.devansh.noteapp.core.entity.ServerError
import com.devansh.noteapp.core.entity.ServerResponse
import com.devansh.noteapp.core.utils.Result
import com.devansh.noteapp.data.models.dto.DeviceSessionInfo
import com.devansh.noteapp.data.models.dto.response.AuthResponse
import com.devansh.noteapp.data.models.dto.response.LoginResponse

interface AuthService {
    suspend fun login(email: String, password: String): Result<LoginResponse, ServerError>
    suspend fun register(email: String, password: String): Result<ServerResponse<String>, ServerError>
    suspend fun refreshAuth(refreshToken: String): Result<AuthResponse, ServerError>

    suspend fun forgotPassword(email: String): Result<Map<String, String>, ServerError>
    suspend fun resendResetOtp(email: String): Result<Map<String, String>, ServerError>
    suspend fun verifyResetOtp(email: String, otp: String): Result<Map<String, String>, ServerError>
    suspend fun resetPassword(token: String, newPassword: String): Result<Map<String, String>, ServerError>

    suspend fun getDevices(): Result<List<DeviceSessionInfo>, ServerError>
    suspend fun logoutAllDevices(): Result<Map<String, String>, ServerError>
}
