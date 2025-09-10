package com.devansh.noteapp.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.ui.screens.setting.AppTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ThemeStatelessViewModel(
    private val appCacheSetting: AppCacheSetting
) : ViewModel() {

    val theme = appCacheSetting.theme

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