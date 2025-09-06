package com.devansh.noteapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

internal val LocalAppTheme = staticCompositionLocalOf<Theme> { error("No theme found") }

@Stable
interface Theme {
    val id: String
    val dark: Boolean
    val colorScheme: ColorScheme
}

object NoteThemes{
    val Light = DsTheme(
        id = "light",
        dark = false,
        colorScheme = LightColorScheme
    )

    val Dark = DsTheme(
        id = "dark",
        dark = true,
        colorScheme = DarkColorScheme
    )
}

@Immutable
data class DsTheme(
    override val id: String,
    override val dark: Boolean,
    override val colorScheme: ColorScheme,
) : Theme {

    val error: Color = colorScheme.error
    val surface: Color = colorScheme.surface
    val onSurface: Color = colorScheme.onSurface
    val surfaceVariant: Color = colorScheme.surfaceVariant
    val surfaceContainerHigh: Color = colorScheme.surfaceContainerHigh
    val onSurfaceVariant: Color = colorScheme.onSurfaceVariant

    val background = colorScheme.background
    val onBackground = colorScheme.onBackground

    val highlightPrimary: Color = colorScheme.onSurface.copy(alpha = 0.15f)
    val highlightSecondary: Color = colorScheme.onSurface.copy(alpha = 0.3f)

    val topBlur by lazy {
        Brush.verticalGradient(
            listOf(
                surface,
                surface.copy(alpha = 0.95f),
                surface.copy(alpha = 0.9f),
                surface.copy(alpha = 0.85f),
            )
        )
    }

    val bottomBlur by lazy {
        Brush.verticalGradient(
            listOf(
                surface.copy(alpha = 0.85f),
                surface.copy(alpha = 0.9f),
                surface.copy(alpha = 0.95f),
                surface
            )
        )
    }

    val shimmerColors by lazy {
        listOf(
            onSurface.copy(alpha = 0.1f),
            onSurface.copy(alpha = 0.05f),
            onSurface.copy(alpha = 0.08f)
        )
    }
}


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