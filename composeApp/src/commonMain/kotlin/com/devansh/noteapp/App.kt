package com.devansh.noteapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.devansh.noteapp.di.appModules
import com.devansh.noteapp.di.platform_di.getFirebaseRemoteConfigReader
import com.devansh.noteapp.navigation.MainNavigationGraph
import com.devansh.noteapp.ui.theme.ThemeProvider
import com.devansh.noteapp.ui.theme.ThemeStatelessViewModel
import kotlinx.coroutines.delay
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinApplication(application = { modules(appModules) }) {
        val viewModel: ThemeStatelessViewModel = koinViewModel()
        LaunchedEffect(Unit){
            delay(4000)
            getFirebaseRemoteConfigReader().isForceUpdateEnabled()
        }
        ThemeProvider(viewModel.state) { MainNavigationGraph() }
    }
}