package com.devansh.noteapp.ui.utils

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

sealed interface UiState<out T> {
    data class Idle<out T>(val data: T) : UiState<T>
    data object Loading : UiState<Nothing>
    data class Success(val message: String?) : UiState<Nothing>
    @OptIn(ExperimentalTime::class)
    data class Error(val message: String?, val timestamp: Instant = Clock.System.now()) :
        UiState<Nothing>
    data class ValidationError(val errors: Map<String, List<String>>) : UiState<Nothing>
}