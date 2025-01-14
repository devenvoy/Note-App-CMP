package com.devansh.noteapp.ui.components

import com.devansh.noteapp.data.entity.ServerResponse
import com.devansh.noteapp.domain.model.AuthResponse

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String?) : UiState<Nothing>()
    data class ValidationError(val errors: Map<String, List<String>>) : UiState<Nothing>()
}

typealias AuthScreenState = UiState<ServerResponse<AuthResponse>>