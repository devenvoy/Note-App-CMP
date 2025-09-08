package com.devansh.noteapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ThemeProvider(
    state: ThemeState,
    content: @Composable () -> Unit
) {
    val viewModel: ThemeViewModel = koinViewModel()
    LaunchedEffect(state) { viewModel.onBind(state) }
    SystemDarkModeHandler(state::systemDarkMode::set)
    ThemeSwitchHandler(state, content)
}

@Composable
private fun SystemDarkModeHandler(
    onSystemDarkModeChanged: (mode: Boolean) -> Unit
) {
    val systemDarkMode = isSystemInDarkTheme()
    LaunchedEffect(systemDarkMode) {
        onSystemDarkModeChanged(systemDarkMode)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ThemeSwitchHandler(
    state: ThemeState,
    content: @Composable () -> Unit
) {
    val theme = state.currentTheme ?: return
    CompositionLocalProvider(LocalAppTheme provides theme) {
        val fontFamily = state.fontFamily

        MaterialExpressiveTheme(
            typography = MaterialTheme.typography.map(fontFamily) ,
            colorScheme = theme.colorScheme,
            content = content
        )
    }
}
