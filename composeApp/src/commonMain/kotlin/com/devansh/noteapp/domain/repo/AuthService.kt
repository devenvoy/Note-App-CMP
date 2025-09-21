package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.entity.ServerResponse
import com.devansh.noteapp.data.dto.AuthResponse
import com.devansh.noteapp.domain.model.DeviceSessionInfo
import com.devansh.noteapp.domain.model.LoginResponse
import com.devansh.noteapp.domain.utils.Result

interface AuthService {
    suspend fun login(email: String, password: String): Result<LoginResponse, ServerError>
    suspend fun register(email: String, password: String): Result<ServerResponse<String>, ServerError>
    suspend fun refreshAuth(refreshToken: String): Result<AuthResponse, ServerError>

    suspend fun forgotPassword(email: String): Result<Map<String, String>, ServerError>
    suspend fun resendResetOtp(email: String): Result<Map<String, String>, ServerError>
    suspend fun verifyResetOtp(email: String, otp: String): Result<Map<String, String>, ServerError>
    suspend fun resetPassword(token: String, newPassword: String): Result<Map<String, String>, ServerError>

    suspend fun getDevices(authToken: String): Result<List<DeviceSessionInfo>, ServerError>
    suspend fun logoutAllDevices(authToken: String): Result<Map<String, String>, ServerError>
}
