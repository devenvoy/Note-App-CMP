package com.devansh.noteapp.ui.theme

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.domain.utils.UnitCBF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

internal class ThemeViewModel : ViewModel() {

    suspend fun onBind(state: ThemeState) {
        val systemDarkModeFlow = snapshotFlow { state.systemDarkMode }.filterNotNull()
        val currentConfigFlow = snapshotFlow { state.currentConfig }.filterNotNull()

        combine(systemDarkModeFlow, currentConfigFlow) { darkMode, config ->
            val autoDark = config.autoDark
            val theme = when {
                autoDark && !darkMode -> config.lightTheme
                autoDark && darkMode -> config.darkTheme
                else -> config.defaultTheme
            }
            withState {
                state.fontFamily = config.fontFamily
                state.systemDarkMode = darkMode
                state.currentConfig = config
                state.currentTheme = theme
            }
        }.collect {}
    }

    fun withState(block: UnitCBF) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            Snapshot.withMutableSnapshot(block)
        }
    }
}