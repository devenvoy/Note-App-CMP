package com.devansh.noteapp.feature.auth.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.core.utils.UnitCBF
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel



@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SplashScreen(navToHome: UnitCBF, navToAuth: UnitCBF, navToOnBoard: UnitCBF) {

    val viewModel = koinViewModel<SplashScreenViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        SplashUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                ContainedLoadingIndicator(
                    modifier = Modifier.size(100.dp),
                    containerColor = Color.Transparent,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                )
            }
        }

        SplashUiState.NavigateToHome -> {
            LaunchedEffect(Unit) {
                delay(500L)
                navToHome()
            }
        }

        SplashUiState.NavigateToLogin -> {
            LaunchedEffect(Unit) {
                delay(500L)
                if (viewModel.isOnBoardComplete) navToAuth() else navToOnBoard()
            }
        }
    }
}
