package com.devansh.noteapp.data.remote

import com.devansh.noteapp.data.dto.AuthRequest
import com.devansh.noteapp.data.dto.ForgotPasswordRequest
import com.devansh.noteapp.data.dto.RefreshRequest
import com.devansh.noteapp.data.dto.ResetPasswordWithTokenRequest
import com.devansh.noteapp.data.dto.VerifyOtpRequest
import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.model.AuthResponse
import com.devansh.noteapp.domain.model.DeviceSessionInfo
import com.devansh.noteapp.domain.model.LoginResponse
import com.devansh.noteapp.domain.repo.AuthService
import com.devansh.noteapp.domain.utils.BaseGateway
import com.devansh.noteapp.domain.utils.Result
import com.jignesh.society.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

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
            post(BuildConfig.BASE_URL + login) {
                contentType(ContentType.Application.Json)
                setBody(AuthRequest(email, password))
            }
        }
    }

    override suspend fun register(email: String, password: String): Result<Unit, ServerError> {
        return tryToExecute {
            post(BuildConfig.BASE_URL + register) {
                contentType(ContentType.Application.Json)
                setBody(AuthRequest(email, password))
            }
        }
    }

    override suspend fun refreshAuth(refreshToken: String): Result<AuthResponse, ServerError> {
        return tryToExecute {
            post(BuildConfig.BASE_URL + refresh) {
                contentType(ContentType.Application.Json)
                setBody(RefreshRequest(refreshToken))
            }
        }
    }

    override suspend fun forgotPassword(email: String): Result<Map<String, String>, ServerError> {
        return tryToExecute {
            post(BuildConfig.BASE_URL + forgotPassword) {
                contentType(ContentType.Application.Json)
                setBody(ForgotPasswordRequest(email))
            }
        }
    }

    override suspend fun resendResetOtp(email: String): Result<Map<String, String>, ServerError> {
        return tryToExecute {
            post(BuildConfig.BASE_URL + resendResetOtp) {
                contentType(ContentType.Application.Json)
                setBody(ForgotPasswordRequest(email))
            }
        }
    }

    override suspend fun verifyResetOtp(
        email: String,
        otp: String
    ): Result<Map<String, String>, ServerError> {
        return tryToExecute {
            post(BuildConfig.BASE_URL + verifyResetOtp) {
                contentType(ContentType.Application.Json)
                setBody(VerifyOtpRequest(email, otp))
            }
        }
    }

    override suspend fun resetPassword(
        token: String,
        newPassword: String
    ): Result<Map<String, String>, ServerError> {
        return tryToExecute {
            post(BuildConfig.BASE_URL + resetPassword) {
                contentType(ContentType.Application.Json)
                setBody(ResetPasswordWithTokenRequest(token, newPassword))
            }
        }
    }

    override suspend fun getDevices(authToken: String): Result<List<DeviceSessionInfo>, ServerError> {
        return tryToExecute {
            get(BuildConfig.BASE_URL + devices) {
                header(HttpHeaders.Authorization, "Bearer $authToken")
            }
        }
    }

    override suspend fun logoutAllDevices(authToken: String): Result<Map<String, String>, ServerError> {
        return tryToExecute {
            delete(BuildConfig.BASE_URL + devices) {
                header(HttpHeaders.Authorization, "Bearer $authToken")
            }
        }
    }
}













