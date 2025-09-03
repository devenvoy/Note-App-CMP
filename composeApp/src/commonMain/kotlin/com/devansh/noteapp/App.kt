package com.devansh.noteapp

import androidx.compose.runtime.Composable
import com.devansh.noteapp.di.appModules
import com.devansh.noteapp.navigation.MainNavigationGraph
import com.devansh.noteapp.ui.theme.ThemeProvider
import com.devansh.noteapp.ui.theme.ThemeStatelessViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinApplication(application = { modules(appModules) }) {
        val viewModel: ThemeStatelessViewModel = koinViewModel()
        ThemeProvider(viewModel.state) { MainNavigationGraph() }
    }
}