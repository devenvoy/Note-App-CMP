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
        _loginEmail.value = email
    }

    fun onLoginPasswordChange(password: String) {
        _loginPassword.value = password
    }

    fun onRegisterEmailChange(email: String) {
        _registerEmail.value = email
    }

    fun onRegisterPasswordChange(password: String) {
        _registerPassword.value = password
    }

    fun onRegisterConfirmPasswordChange(password: String) {
        _registerConfirmPwd.value = password
    }

    fun login() {
        screenModelScope.launch {
            _authState.value = UiState.Loading
            val result = authDao.login(_loginEmail.value, _loginPassword.value)
            result.onSuccess {
                if(it.value == null) return@launch
                pref.accessToken = it.value.authToken
                _authState.value = UiState.Success(it)
            }.onError {
                _authState.value = UiState.Error(it.detail)
            }
        }
    }

    fun register() {
        screenModelScope.launch {
            _authState.value = UiState.Loading
            val result = authDao.register(_registerEmail.value, _registerPassword.value)
            result.onSuccess { response ->
                pref.accessToken = response.value?.authToken.toString()
                _authState.value = UiState.Success(response)
            }.onError {
                _authState.value = UiState.Error(it.detail)
            }
        }
    }
}