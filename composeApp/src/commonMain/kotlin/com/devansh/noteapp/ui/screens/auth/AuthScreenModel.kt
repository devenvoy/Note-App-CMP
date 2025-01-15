package com.devansh.noteapp.ui.screens.auth

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.devansh.noteapp.data.remote.utils.onError
import com.devansh.noteapp.data.remote.utils.onSuccess
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.AuthDao
import com.devansh.noteapp.ui.components.AuthScreenState
import com.devansh.noteapp.ui.components.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthScreenModel(
    private val pref: AppCacheSetting,
    private val authDao: AuthDao
) : ScreenModel {

    private val _loginEmail = MutableStateFlow("")
    val loginEmail = _loginEmail.asStateFlow()

    private val _loginPassword = MutableStateFlow("")
    val loginPassword = _loginPassword.asStateFlow()

    private val _registerEmail = MutableStateFlow("")
    val registerEmail = _registerEmail.asStateFlow()

    private val _registerPassword = MutableStateFlow("")
    val registerPassword = _registerPassword.asStateFlow()

    private val _registerConfirmPwd = MutableStateFlow("")
    val registerConfirmPwd = _registerConfirmPwd.asStateFlow()

    private val _authState = MutableStateFlow<AuthScreenState>(UiState.Idle)
    val authState = _authState.asStateFlow()

    fun onLoginEmailChange(email: String) {
        _loginEmail.update { email }
    }

    fun onLoginPasswordChange(password: String) {
        _loginPassword.update { password }
    }

    fun onRegisterEmailChange(email: String) {
        _registerEmail.update { email }
    }

    fun onRegisterPasswordChange(password: String) {
        _registerPassword.update { password }
    }

    fun onRegisterConfirmPasswordChange(password: String) {
        _registerConfirmPwd.update { password }
    }

    fun login() {
        _authState.update { UiState.Loading }
        screenModelScope.launch {
            if (!validateLoginInputs()) return@launch
            val result = authDao.login(_loginEmail.value, _loginPassword.value)
            result.onSuccess { res ->
                if (res.value == null) return@launch
                pref.accessToken = res.value.authToken
                _authState.update { UiState.Success(res) }
            }.onError { e ->
                _authState.update { UiState.Error(e.detail) }
            }
        }
    }

    fun register() {
        _authState.update { UiState.Loading }
        screenModelScope.launch {
            if (!validateRegisterInputs()) return@launch
            val result = authDao.register(_registerEmail.value, _registerPassword.value)
            result.onSuccess { response ->
                pref.accessToken = response.value?.authToken.toString()
                _authState.update { UiState.Success(response) }
            }.onError { e->
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
            _loginEmail.value.isBlank() -> {
                _authState.update { UiState.Error("Email cannot be empty") }
                false
            }

            !isValidEmail(_loginEmail.value) -> {
                _authState.update { UiState.Error("Invalid email format") }
                false
            }

            _loginPassword.value.isBlank() -> {
                _authState.update { UiState.Error("Password cannot be empty") }
                false
            }

            else -> true
        }
    }

    private fun validateRegisterInputs(): Boolean {
        return when {
            _registerEmail.value.isBlank() -> {
                _authState.update { UiState.Error("Email cannot be empty") }
                false
            }

            !isValidEmail(_registerEmail.value) -> {
                _authState.update { UiState.Error("Invalid email format") }
                false
            }

            _registerPassword.value.isBlank() -> {
                _authState.update { UiState.Error("Password cannot be empty") }
                false
            }

            _registerPassword.value != _registerConfirmPwd.value -> {
                _authState.update { UiState.Error("Passwords do not match") }
                false
            }

            else -> true
        }
    }
}
