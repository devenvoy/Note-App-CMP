package com.devansh.noteapp.ui.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.navigation.NavRoute
import kotlinx.coroutines.delay
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
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
    var loadingMessage by remember { mutableStateOf("Loading..") }

    LaunchedEffect(Unit) {
        if (screenModel.isUserLoggedIn()) {
            if (screenModel.isSyncAutoEnable()) {
                loadingMessage = "Syncing with database"
                screenModel.syncDatabase()
            }
            loadingMessage = "Finished"
            delay(500L)
            navToHome()
        } else {
            delay(500L)
            navToAuth()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ContainedLoadingIndicator(
                containerColor = Color.Transparent,
                indicatorColor = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(10.sdp))
            Text(
                text = loadingMessage,
                color = Color.Black,
                fontSize = 16.ssp,
            )
        }
    }
}
