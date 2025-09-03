package com.devansh.noteapp.ui.theme

import androidx.lifecycle.ViewModel

class ThemeStatelessViewModel(val state: ThemeState) : ViewModel() {

    init {
        state.currentConfig = state.defaultConfig
    }
}