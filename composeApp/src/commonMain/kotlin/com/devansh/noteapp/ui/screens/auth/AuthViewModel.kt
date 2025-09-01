package com.devansh.noteapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.AuthService
import com.devansh.noteapp.domain.utils.onFailure
import com.devansh.noteapp.domain.utils.onSuccess
import com.devansh.noteapp.ui.components.UiState
import io.github.jan.supabase.compose.auth.ui.password.PasswordRule
import io.github.jan.supabase.compose.auth.ui.password.PasswordRuleResult
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

    val passwordRules = listOf(
        PasswordRule.minLength(8),
        PasswordRule.containsSpecialCharacter(),
        PasswordRule.containsDigit(),
        PasswordRule.containsLowercase(),
        PasswordRule.containsUppercase()
    )


    fun onEmailChange(email: String) = _email.update { email }
    fun onPasswordChange(password: String) = _password.update { password }
    fun onConfirmPasswordChanged(password: String) = _confirmPassword.update { password }

    fun login() {
        _authState.update { UiState.Loading }
        viewModelScope.launch {
            if (!validateInputs(false)) return@launch
            val result = authService.login(_email.value, _password.value)
            result.onSuccess { res ->
                pref.accessToken = res.accessToken
                pref.refreshToken = res.refreshToken
                pref.setUserEmail(_email.value)
                _authState.update { UiState.Success("Logged in successfully") }
            }.onFailure { e ->
                _authState.update { UiState.Error(e.detail) }
            }
        }
    }

    fun register() {
        _authState.update { UiState.Loading }
        viewModelScope.launch {
            if (!validateInputs(true)) return@launch
            val result = authService.register(_email.value, _password.value)
            result.onSuccess { response ->
                val loginResult = authService.login(_email.value, _password.value)
                loginResult.onSuccess { res ->
                    pref.accessToken = res.accessToken
                    pref.refreshToken = res.refreshToken
                    pref.setUserEmail(_email.value)
                    _authState.update { UiState.Success("Registered and Logged in successfully") }
                }.onFailure { e ->
                    _authState.update { UiState.Error(e.detail) }
                }
            }.onFailure { e ->
                _authState.update { UiState.Error(e.detail) }
            }
        }
    }


    private fun validateInputs(requireConfirm: Boolean): Boolean {
        if (_email.value.isBlank()) return error("Email cannot be empty")
        if (!isValidEmail(_email.value)) return error("Invalid email format")

        val passwordResult = passwordRules.map {
            PasswordRuleResult(it.description, it.predicate(_password.value))
        }
        if (passwordResult.any { !it.isFulfilled }) {
            return error(passwordResult.first { !it.isFulfilled }.description)
        }

        if (requireConfirm && _password.value != _confirmPassword.value) {
            return error("Confirm password and password do not match")
        }

        return true
    }

    private fun error(msg: String): Boolean {
        _authState.update { UiState.Error(msg) }
        return false
    }

    private fun isValidEmail(email: String): Boolean =
        email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
}
