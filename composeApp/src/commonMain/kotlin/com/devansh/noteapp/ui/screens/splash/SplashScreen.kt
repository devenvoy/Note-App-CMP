package com.devansh.noteapp.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.navigation.NavRoute
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.splashScreen(navController: NavHostController) {
    composable<NavRoute.SplashScreen> {
        SplashScreen(
            navToHome = { navController.navigate(NavRoute.HomeScreen) },
            navToAuth = { navController.navigate(NavRoute.Auth) }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SplashScreen(navToHome: UnitCBF, navToAuth: UnitCBF) {

    val screenModel = koinViewModel<SplashScreenModel>()

    LaunchedEffect(Unit) {
        if (screenModel.isUserLoggedIn()) {
            if (screenModel.isSyncAutoEnable()) {
                screenModel.syncDatabase()
            }
            delay(500L)
            navToHome()
        } else {
            delay(500L)
            navToAuth()
        }
    }

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
