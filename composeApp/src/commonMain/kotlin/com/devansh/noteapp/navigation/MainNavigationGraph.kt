package com.devansh.noteapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.devansh.noteapp.ui.screens.add_edit_note.addNoteScreen
import com.devansh.noteapp.ui.screens.auth.authScreen
import com.devansh.noteapp.ui.screens.home.homeScreen
import com.devansh.noteapp.ui.screens.setting.settingsScreen
import com.devansh.noteapp.ui.screens.splash.splashScreen


@Composable
fun MainNavigationGraph(
    mainNavController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = mainNavController,
        startDestination = NavRoute.HomeScreen
    ) {
        splashScreen(mainNavController)

        authScreen(mainNavController)

        homeScreen(mainNavController)

        addNoteScreen(mainNavController)

        settingsScreen(mainNavController)

    }
}