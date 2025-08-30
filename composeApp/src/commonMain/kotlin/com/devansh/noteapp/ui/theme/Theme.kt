package com.devansh.noteapp.ui.theme

import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.font.FontFamily
import com.materialkolor.LocalDynamicMaterialThemeSeed
import com.materialkolor.PaletteStyle
import com.materialkolor.ktx.animateColorScheme
import com.materialkolor.rememberDynamicMaterialThemeState
import note_app_cmp.composeapp.generated.resources.Res
import note_app_cmp.composeapp.generated.resources.geomanist_regular
import org.jetbrains.compose.resources.Font

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


/*@Composable
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
}*/


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NoteAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val dynamicThemeState = rememberDynamicMaterialThemeState(
        isDark = darkTheme,
        style = PaletteStyle.Neutral,
        primary = LightPrimary,
    )

    val colorScheme = dynamicThemeState.colorScheme
    val scheme = if (!false) {
        colorScheme
    } else {
            animateColorScheme(colorScheme = colorScheme, animationSpec = { spring() })
        }

    val geoManistRegular = Font(Res.font.geomanist_regular)
    val geoManistMedium = Font(Res.font.geomanist_regular)
    CompositionLocalProvider(LocalDynamicMaterialThemeSeed provides dynamicThemeState.seedColor) {
        MaterialExpressiveTheme(
            typography = MaterialTheme.typography.map(
                FontFamily(listOf(geoManistRegular, geoManistMedium))
            ),
            colorScheme = scheme,
            content = content,
        )
    }
}