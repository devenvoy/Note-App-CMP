package com.devansh.noteapp.data.repository.impl

import com.devansh.noteapp.core.entity.ServerError
import com.devansh.noteapp.core.entity.ServerResponse
import com.devansh.noteapp.core.network.BaseGateway
import com.devansh.noteapp.core.utils.Result
import com.devansh.noteapp.data.models.dto.DeviceSessionInfo
import com.devansh.noteapp.data.models.dto.request.AuthRequest
import com.devansh.noteapp.data.models.dto.request.ForgotPasswordRequest
import com.devansh.noteapp.data.models.dto.request.RefreshRequest
import com.devansh.noteapp.data.models.dto.request.ResetPasswordWithTokenRequest
import com.devansh.noteapp.data.models.dto.request.VerifyOtpRequest
import com.devansh.noteapp.data.models.dto.response.AuthResponse
import com.devansh.noteapp.data.models.dto.response.LoginResponse
import com.devansh.noteapp.data.repository.repo.AuthService
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthServiceImpl(private val httpClient: HttpClient) : AuthService, BaseGateway(httpClient) {

    private val login = "/auth/login"
    private val register = "/auth/register"
    private val refresh = "/auth/refresh"
    private val forgotPassword = "/auth/forgot-password"
    private val resendResetOtp = "/auth/resend-reset-otp"
    private val verifyResetOtp = "/auth/verify-reset-otp"
    private val resetPassword = "/auth/reset-password"
    private val devices = "/auth/devices"

    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginResponse, ServerError> {
        return tryToExecute {
            post(login) { setBody(AuthRequest(email, password)) }
        }
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<ServerResponse<String>, ServerError> {
        return tryToExecute {
            post(register) { setBody(AuthRequest(email, password)) }
        }
    }

    override suspend fun refreshAuth(refreshToken: String): Result<AuthResponse, ServerError> {
        return tryToExecute {
            post(refresh) { setBody(RefreshRequest(refreshToken)) }
        }
    }

    override suspend fun forgotPassword(email: String): Result<Map<String, String>, ServerError> {
        return tryToExecute {
            post(forgotPassword) { setBody(ForgotPasswordRequest(email)) }
        }
    }

    override suspend fun resendResetOtp(email: String): Result<Map<String, String>, ServerError> {
        return tryToExecute {
            post(resendResetOtp) { setBody(ForgotPasswordRequest(email)) }
        }
    }

    override suspend fun verifyResetOtp(
        email: String,
        otp: String
    ): Result<Map<String, String>, ServerError> {
        return tryToExecute {
            post(verifyResetOtp) { setBody(VerifyOtpRequest(email, otp)) }
        }
    }

    override suspend fun resetPassword(
        token: String,
        newPassword: String
    ): Result<Map<String, String>, ServerError> {
        return tryToExecute {
            post(resetPassword) { setBody(ResetPasswordWithTokenRequest(token, newPassword)) }
        }
    }

    override suspend fun getDevices(): Result<List<DeviceSessionInfo>, ServerError> {
        return tryToExecute {
            get(devices)
        }
    }

    override suspend fun logoutAllDevices(): Result<Map<String, String>, ServerError> {
        return tryToExecute {
            delete(devices)
        }
    }
}













