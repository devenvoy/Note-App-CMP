package com.devansh.noteapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.devansh.noteapp.core.designsystem.SettingsState
import com.devansh.noteapp.core.designsystem.utils.LocalDeviceConfiguration
import com.devansh.noteapp.core.utils.UnitCBF
import com.devansh.noteapp.di.shareText
import com.devansh.noteapp.feature.notes.presentation.add_edit.AddEditNoteViewModel
import com.devansh.noteapp.feature.notes.presentation.add_edit.AddEditScreenContent
import com.devansh.noteapp.feature.notes.presentation.category.CategoryScreen
import com.devansh.noteapp.feature.notes.presentation.notes.HomeScreenContent
import com.devansh.noteapp.feature.notes.presentation.notes.HomeScreenViewModel
import com.devansh.noteapp.feature.notes.presentation.notes.NotesListDetailScreen
import com.devansh.noteapp.feature.settings.presentation.BaseScreenViewModel
import com.devansh.noteapp.feature.settings.presentation.SettingScreenContent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.categoryScreen(mainNavController: NavHostController, navigateToHome: UnitCBF) {
    composable<NavRoute.Category> {
        CategoryScreen(navigateUp=navigateToHome)
    }
}

fun NavGraphBuilder.homeScreen(settingsState: SettingsState,mainNavController: NavHostController) {
    composable<NavRoute.HomeScreen> {
        val homeScreenModel = koinViewModel<HomeScreenViewModel>()
        val deviceConfiguration = LocalDeviceConfiguration.current
        if (deviceConfiguration.isMobile()) {
            HomeScreenContent(
                viewModel = homeScreenModel,
                settingsState = settingsState,
                onNavigateToAddEditNote = { id -> mainNavController.navigate(NavRoute.AddNote(id)) },
                onNavigateToSettings = { mainNavController.navigate(NavRoute.Setting) },
                onShareText = { shareText(it, "text/plain") }
            )
        } else {
            NotesListDetailScreen(
                homeScreenModel = homeScreenModel,
                settingsState = settingsState,
                onNavigateToSettings = { mainNavController.navigate(NavRoute.Setting) },
                onShareText = { shareText(it, "text/plain") }
            )
        }
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

fun NavGraphBuilder.settingsScreen(baseScreenViewModel: BaseScreenViewModel,navHostController: NavHostController) {
    composable<NavRoute.Setting> {
        SettingScreenContent(
            viewModel = baseScreenViewModel,
            navigateBack = { navHostController.navigateUp() },
            logOut = {
                baseScreenViewModel.logOut()
                navHostController.navigate(NavRoute.Auth) {
                    popUpTo(NavRoute.BaseScreen) { inclusive = true }
                }
            }
        )
    }
}
