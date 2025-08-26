package com.devansh.noteapp

import androidx.compose.runtime.Composable
import com.devansh.noteapp.di.appModules
import com.devansh.noteapp.ui.screens.splash.SplashScreen
import com.devansh.noteapp.ui.theme.NoteAppTheme
import org.koin.compose.KoinApplication

@Composable
fun App() {
    NoteAppTheme {
        KoinApplication(application = { modules(appModules) }) { SplashScreen() }
    }
}