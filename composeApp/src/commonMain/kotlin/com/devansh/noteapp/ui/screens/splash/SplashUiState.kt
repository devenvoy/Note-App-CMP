package com.devansh.noteapp.ui.screens.splash

sealed interface SplashUiState {
    object Loading : SplashUiState
    object NavigateToHome : SplashUiState
    object NavigateToLogin : SplashUiState
}
