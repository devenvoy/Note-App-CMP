package com.devansh.noteapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkOnPrimary,
    onSecondary = DarkOnSecondary,
    onTertiary = DarkOnTertiary,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    tertiary = LightTertiary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = LightOnPrimary,
    onSecondary = LightOnSecondary,
    onTertiary = LightOnTertiary,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
)


@Composable
fun NoteAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}