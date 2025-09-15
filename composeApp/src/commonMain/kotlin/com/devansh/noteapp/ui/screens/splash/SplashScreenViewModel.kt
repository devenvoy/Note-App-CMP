package com.devansh.noteapp.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.AuthService
import com.devansh.noteapp.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val pref: AppCacheSetting,
    private val authService: AuthService
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState

    val isSyncAutoEnable = pref.autoSyncDB
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isOnBoardComplete = pref.isOnBoardComplete

    init {
        checkAuth()
    }

    fun checkAuth() {
        viewModelScope.launch {
            val refreshToken = pref.refreshToken

            if (refreshToken.isNullOrBlank()) {
                _uiState.value = SplashUiState.NavigateToLogin
                return@launch
            }

            when (val result = authService.refreshAuth(refreshToken)) {
                is Result.Success -> {
                    pref.accessToken = result.data.accessToken
                    pref.refreshToken = result.data.refreshToken
                    _uiState.update { SplashUiState.NavigateToHome }
                }

                is Result.Failure -> {
                    pref.accessToken = null
                    pref.refreshToken = null
                    _uiState.update { SplashUiState.NavigateToLogin }
                }

                Result.Loading -> _uiState.update { SplashUiState.Loading }
            }
        }
    }
}