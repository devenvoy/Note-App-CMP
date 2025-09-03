package com.devansh.noteapp.ui.screens.categoryFolder

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.navigation.NavRoute


fun NavGraphBuilder.categoryScreen(mainNavController: NavHostController) {
    composable<NavRoute.Category> {
        CategoryScreen()
    }
}

@Composable
fun CategoryScreen() {
}