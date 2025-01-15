package com.devansh.noteapp.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dokar.sonner.TextToastAction
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterDefaults
import com.dokar.sonner.rememberToasterState


@Composable
fun <T> UiStateHandler(
    uiState: UiState<T>,
    onErrorShowed: () -> Unit,
    content: @Composable (T) -> Unit
) {
    val toaster = rememberToasterState()

    when (uiState) {
        is UiState.Error -> {
            LaunchedEffect(uiState.timestamp) {
                toaster.show(
                    message = uiState.message ?: "Unknown Error",
                    duration = ToasterDefaults.DurationLong,
                    type = ToastType.Error
                )
                onErrorShowed()
            }
            Toaster(
                state = toaster,
                richColors = true,
                darkTheme = isSystemInDarkTheme(),
                showCloseButton = true,
                alignment = Alignment.TopCenter,
            )
        }

        is UiState.ValidationError -> {
            val errors = uiState.errors
            LaunchedEffect(errors) {
                errors.entries.forEach { eMsg ->
                    toaster.show(
                        message = eMsg,
                        duration = ToasterDefaults.DurationShort,
                        type = ToastType.Error
                    )
                }
                onErrorShowed()
            }
            Toaster(
                state = toaster,
                richColors = true,
                darkTheme = isSystemInDarkTheme(),
                showCloseButton = true,
                alignment = Alignment.TopCenter,
            )
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