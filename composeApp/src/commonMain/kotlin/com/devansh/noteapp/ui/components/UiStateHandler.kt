package com.devansh.noteapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun <T> UiStateHandler(
    uiState: UiState<T>,
    onErrorShowed: () -> Unit,
    content: @Composable (T) -> Unit // Handle Success state
) {

    when (uiState) {
        is UiState.Error -> {
            LaunchedEffect(Unit) {
               /* showToast(
                    message = uiState.message ?: "Unknown Error",
                    backgroundColor = Color.Red,
                    textColor = Color.White,
                    duration = ToastDuration.Short,
                    bottomPadding = 100
                )*/
                onErrorShowed()
            }
        }

        is UiState.ValidationError -> {
            val errors = uiState.errors
            LaunchedEffect(Unit) {
               /* showToast(
                    message = errors.entries.first().value.first(),
                    backgroundColor = Color.Red,
                    textColor = Color.White,
                    duration = ToastDuration.Short,
                    bottomPadding = 100
                )*/
                onErrorShowed()
            }
        }

        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            content(uiState.data)
        }

        UiState.Idle -> {}
    }
}