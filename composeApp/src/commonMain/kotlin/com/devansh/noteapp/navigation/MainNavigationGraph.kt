package com.devansh.noteapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.devansh.noteapp.ui.screens.auth.authScreen
import com.devansh.noteapp.ui.screens.home.homeScreen
import com.devansh.noteapp.ui.screens.splash.splashScreen


@Composable
fun MainNavigationGraph(
    mainNavController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = mainNavController,
        startDestination = NavRoute.SplashScreen
    ) {
        splashScreen(mainNavController)

        authScreen(mainNavController)

        homeScreen(mainNavController)

    }
}