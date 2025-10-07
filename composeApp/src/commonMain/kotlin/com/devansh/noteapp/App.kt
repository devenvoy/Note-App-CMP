package com.devansh.noteapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.devansh.noteapp.core.designsystem.theme.ThemeProvider
import com.devansh.noteapp.core.designsystem.theme.ThemeStatelessViewModel
import com.devansh.noteapp.di.appModules
import com.devansh.noteapp.navigation.MainNavigationGraph
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(navController: NavHostController = rememberNavController()) {
    KoinApplication(application = { modules(appModules) }) {
        val viewModel: ThemeStatelessViewModel = koinViewModel()
        ThemeProvider(viewModel.state) { MainNavigationGraph(navController) }
    }
}