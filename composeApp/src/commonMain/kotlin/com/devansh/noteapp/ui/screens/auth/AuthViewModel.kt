package com.devansh.noteapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.AuthService
import com.devansh.noteapp.domain.utils.onFailure
import com.devansh.noteapp.domain.utils.onSuccess
import com.devansh.noteapp.ui.components.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

typealias AuthScreenState = UiState<String>

@OptIn(ExperimentalTime::class)
class AuthViewModel(
    private val pref: AppCacheSetting,
    private val authService: AuthService
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _authState = MutableStateFlow<AuthScreenState>(UiState.Idle)
    val authState = _authState.asStateFlow()

    fun onEmailChange(email: String) {
        _email.update { email }
    }

    fun onPasswordChange(password: String) {
        _password.update { password }
    }

    fun onConfirmPasswordChanged(password: String) {
        _confirmPassword.update { password }
    }

    fun login() {
        _authState.update { UiState.Loading }
        viewModelScope.launch {
            if (!validateLoginInputs()) return@launch
            val result = authService.login(_email.value, _password.value)
            result.onSuccess { res ->
                pref.accessToken = res.accessToken
                _authState.update { UiState.Success("Logged in successfully") }
            }.onFailure { e ->
                _authState.update { UiState.Error(e.detail) }
            }
        }
    }

    fun register() {
        _authState.update { UiState.Loading }
        viewModelScope.launch {
            if (!validateRegisterInputs()) return@launch
            val result = authService.login(_email.value, _password.value)
            result.onSuccess { response ->
                pref.accessToken = response.accessToken
                _authState.update { UiState.Success("Account created successfully") }
            }.onFailure { e ->
                _authState.update { UiState.Error(e.detail) }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(Regex(emailRegex))
    }

    private fun validateLoginInputs(): Boolean {
        return when {
            _email.value.isBlank() -> {
                _authState.update { UiState.Error("Email cannot be empty") }
                false
            }

            !isValidEmail(_email.value) -> {
                _authState.update { UiState.Error("Invalid email format") }
                false
            }

            _password.value.isBlank() -> {
                _authState.update { UiState.Error("Password cannot be empty") }
                false
            }

            else -> true
        }
    }

    private fun validateRegisterInputs(): Boolean {
        return when {
            _email.value.isBlank() -> {
                _authState.update { UiState.Error("Email cannot be empty") }
                false
            }

            !isValidEmail(_email.value) -> {
                _authState.update { UiState.Error("Invalid email format") }
                false
            }

            _password.value.isBlank() -> {
                _authState.update { UiState.Error("Password cannot be empty") }
                false
            }

            _password.value != _confirmPassword.value -> {
                _authState.update { UiState.Error("Passwords do not match") }
                false
            }

            else -> true
        }
    }
}
