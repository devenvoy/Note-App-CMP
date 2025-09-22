package com.devansh.noteapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.core.designsystem.utils.ScreenTransitions
import com.devansh.noteapp.feature.auth.presentation.onboard.OnBoardScreen
import com.devansh.noteapp.feature.auth.presentation.splash.SplashScreen
import com.devansh.noteapp.feature.authentication.ForgotPasswordScreen
import com.devansh.noteapp.feature.authentication.LoginScreen
import com.devansh.noteapp.feature.authentication.RegisterScreenContent
import com.devansh.noteapp.feature.authentication.ResetPasswordScreen


fun NavGraphBuilder.splashScreen(navController: NavHostController) {
    composable<NavRoute.SplashScreen> {
        SplashScreen(
            navToHome = {
                navController.navigate(NavRoute.BaseScreen) {
                    popUpTo(NavRoute.SplashScreen) { inclusive = true }
                }
            },
            navToAuth = {
                navController.navigate(NavRoute.Auth) {
                    popUpTo(NavRoute.SplashScreen) { inclusive = true }
                }
            },
            navToOnBoard = {
                navController.navigate(NavRoute.OnBoardScreen) {
                    popUpTo(NavRoute.SplashScreen) { inclusive = true }
                }
            }
        )
    }
}

fun NavGraphBuilder.onBoardScreen(mainNavController: NavHostController) {
    composable<NavRoute.OnBoardScreen> {
        OnBoardScreen(
            navToLogin = {
                mainNavController.navigate(NavRoute.Auth) {
                    popUpTo(NavRoute.OnBoardScreen) { inclusive = true }
                }
            }
        )
    }
}


fun NavGraphBuilder.resetPasswordScreen(
    mainNavController: NavHostController
) {
    composable<NavRoute.ResetPassword> {
        ResetPasswordScreen(
            onNavigateToLogin = {
                mainNavController.navigate(NavRoute.Auth) {
                    popUpTo(NavRoute.ForgotPassword) { inclusive = true }
                }
            }
        )
    }
}


fun NavGraphBuilder.forgotPasswordScreen(mainNavController: NavHostController) {
    composable<NavRoute.ForgotPassword> {
        ForgotPasswordScreen(
            onSuccess = {
                mainNavController.navigate(NavRoute.Auth) {
                    popUpTo(NavRoute.ForgotPassword) { inclusive = true }
                }
            },
            onNavBack = { mainNavController.navigateUp() }
        )
    }
}


fun NavGraphBuilder.loginScreen(
    navHostController: NavHostController
) {
    composable<NavRoute.Login> {
        LoginScreen(
            onSuccess = {
                navHostController.navigate(NavRoute.BaseScreen) {
                    popUpTo<NavRoute.Auth> { inclusive = true }
                }
            },
            navToForgotPassword = { navHostController.navigate(NavRoute.ForgotPassword) },
            navToRegister = { navHostController.navigate(NavRoute.Register) }
        )
    }
}


fun NavGraphBuilder.registerScreen(
    mainNavController: NavHostController
) {
    composable<NavRoute.Register>(
        enterTransition = { ScreenTransitions.slideInFromBottom },
//        exitTransition = { ScreenTransitions.slideOutToBottom },
        popEnterTransition = { ScreenTransitions.slideInFromBottom },
//        popExitTransition = { ScreenTransitions.slideOutToBottom }
    ) {
        RegisterScreenContent(onSuccess = {
            mainNavController.navigate(NavRoute.Login) {
                popUpTo<NavRoute.Register> { inclusive = true }
            }
        }, navigateBack = { mainNavController.navigateUp() })
    }
}
