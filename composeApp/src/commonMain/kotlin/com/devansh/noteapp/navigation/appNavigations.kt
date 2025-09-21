package com.devansh.noteapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.devansh.noteapp.core.utils.UnitCBF
import com.devansh.noteapp.di.shareText
import com.devansh.noteapp.feature.notes.presentation.add_edit.AddEditNoteViewModel
import com.devansh.noteapp.feature.notes.presentation.add_edit.AddEditScreenContent
import com.devansh.noteapp.feature.notes.presentation.category.CategoryScreen
import com.devansh.noteapp.feature.notes.presentation.notes.HomeScreenContent
import com.devansh.noteapp.feature.notes.presentation.notes.HomeScreenViewModel
import com.devansh.noteapp.feature.settings.presentation.SettingScreenContent
import com.devansh.noteapp.feature.settings.presentation.SettingViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.categoryScreen(mainNavController: NavHostController, navigateToHome: UnitCBF) {
    composable<NavRoute.Category> {
        CategoryScreen(navigateToHome)
    }
}

fun NavGraphBuilder.homeScreen(mainNavController: NavHostController) {
    composable<NavRoute.HomeScreen> {
        val homeScreenModel = koinViewModel<HomeScreenViewModel>()
        HomeScreenContent(
            homeScreenModel = homeScreenModel,
            onNavigateToAddEditNote = { id -> mainNavController.navigate(NavRoute.AddNote(id)) },
            goToSettings = { mainNavController.navigate(NavRoute.Setting) },
            onShareText = { shareText(it, "text/plain") }
        )
    }
}

fun NavGraphBuilder.addNoteScreen(navHostController: NavHostController) {
    composable<NavRoute.AddNote> {
        val noteId = it.toRoute<NavRoute.AddNote>().noteId
        val viewModel = koinViewModel<AddEditNoteViewModel> { parametersOf(noteId) }
        AddEditScreenContent(
            viewModel = viewModel, onNavigateUp = { navHostController.navigateUp() })
    }
}

fun NavGraphBuilder.settingsScreen(navHostController: NavHostController) {
    composable<NavRoute.Setting> {
        val viewModel = koinViewModel<SettingViewModel>()
        SettingScreenContent(
            viewModel = viewModel,
            navigateBack = { navHostController.navigateUp() },
            logOut = {
                viewModel.logOut()
                navHostController.navigate(NavRoute.Auth) {
                    popUpTo(NavRoute.BaseScreen) { inclusive = true }
                }
            }
        )
    }
}
