package com.devansh.noteapp.core.designsystem.theme

import androidx.compose.ui.text.font.FontFamily

data class ThemeConfig(
    val defaultTheme: Theme,
    val autoDark: Boolean = true,
    val darkTheme: Theme = defaultTheme,
    val lightTheme: Theme = defaultTheme,
    val fontFamily: FontFamily = FontFamily.Default
)