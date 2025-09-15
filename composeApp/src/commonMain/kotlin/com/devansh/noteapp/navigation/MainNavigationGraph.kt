package com.devansh.noteapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.devansh.noteapp.ui.screens.add_edit_note.addNoteScreen
import com.devansh.noteapp.ui.screens.auth.forgotPasswordScreen
import com.devansh.noteapp.ui.screens.auth.loginScreen
import com.devansh.noteapp.ui.screens.auth.registerScreen
import com.devansh.noteapp.ui.screens.auth.resetPasswordScreen
import com.devansh.noteapp.ui.screens.base.baseScreen
import com.devansh.noteapp.ui.screens.onboard.onBoardScreen
import com.devansh.noteapp.ui.screens.setting.settingsScreen
import com.devansh.noteapp.ui.screens.splash.splashScreen


@Composable
fun MainNavigationGraph(
    mainNavController: NavHostController = rememberNavController()
) {

    NavHost(
        modifier = Modifier
            .fillMaxSize(),
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