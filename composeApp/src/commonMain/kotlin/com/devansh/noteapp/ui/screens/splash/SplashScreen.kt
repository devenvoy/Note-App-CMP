package com.devansh.noteapp.ui.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
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
import kotlinx.coroutines.delay
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen() {

//        val navigator = LocalNavigator.currentOrThrow

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
//                navigator.replace(HomeScreen())
        } else {
            delay(500L)
//                navigator.replace(AuthScreen())
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
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(10.sdp))
            Text(
                text = loadingMessage,
                color = Color.Black,
                fontSize = 16.ssp,
            )
        }
    }
}
