package com.devansh.noteapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.devansh.noteapp.base.baseScreen
import com.devansh.noteapp.core.designsystem.utils.LocalDeviceConfiguration
import com.devansh.noteapp.core.utils.DeviceConfiguration

@Composable
fun MainNavigationGraph(
    mainNavController: NavHostController = rememberNavController()
) {

    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val deviceConfiguration =
        DeviceConfiguration.fromWindowSizeClass(windowAdaptiveInfo.windowSizeClass)
    CompositionLocalProvider(LocalDeviceConfiguration provides deviceConfiguration) {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = mainNavController,
            startDestination = NavRoute.SplashScreen
        ) {
            splashScreen(mainNavController)

            onBoardScreen(mainNavController)

            navigation<NavRoute.Auth>(NavRoute.Login) {
                loginScreen(mainNavController)
                registerScreen(mainNavController)
                forgotPasswordScreen(mainNavController)
                resetPasswordScreen(mainNavController)
            }

            baseScreen(mainNavController)

            addNoteScreen(mainNavController)

            settingsScreen(mainNavController)
        }
    }

}