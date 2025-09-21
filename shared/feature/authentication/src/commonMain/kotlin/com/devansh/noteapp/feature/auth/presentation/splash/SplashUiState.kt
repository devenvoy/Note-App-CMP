package com.devansh.noteapp.feature.auth.presentation.splash

sealed interface SplashUiState {
    object Loading : SplashUiState
    object NavigateToHome : SplashUiState
    object NavigateToLogin : SplashUiState
}
