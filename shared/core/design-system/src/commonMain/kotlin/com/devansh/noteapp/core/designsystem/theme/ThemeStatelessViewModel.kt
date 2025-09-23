package com.devansh.noteapp.core.designsystem.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.data.models.dto.settings.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ThemeStatelessViewModel(
    private val theme : Flow<AppTheme>
) : ViewModel() {

    val state: ThemeState = DefaultThemeState(
        defaultConfig = ThemeConfig(
            defaultTheme = NoteThemes.Light,
            lightTheme = NoteThemes.Light,
            darkTheme = NoteThemes.Dark,
        )
    )

    init {
        viewModelScope.launch {
            theme.collectLatest { applyAppTheme(it) }
        }
    }

    private fun applyAppTheme(appTheme: AppTheme) {
        when (appTheme) {
            AppTheme.LIGHT -> state.setLight()
            AppTheme.DARK -> state.setDark()
            AppTheme.SYSTEM -> state.setAuto()
            AppTheme.UNDEFINED -> state.setAuto()
        }
    }
}