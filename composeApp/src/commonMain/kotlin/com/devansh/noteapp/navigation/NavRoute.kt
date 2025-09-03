package com.devansh.noteapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute {

    @Serializable
    data object SplashScreen : NavRoute

    @Serializable
    data object HomeScreen

    @Serializable
    data object Auth : NavRoute

    @Serializable
    data object Setting : NavRoute

    @Serializable
    data class AddNote(val noteId: String? = null) : NavRoute

    @Serializable
    data object Category : NavRoute

    @Serializable
    data object Login : NavRoute

    @Serializable
    data object Register : NavRoute

    @Serializable
    data object ForgotPassword : NavRoute

    @Serializable
    data object ResetPassword : NavRoute

    @Serializable
    data object Profile : NavRoute

    @Serializable
    data object About : NavRoute

    @Serializable
    data object ContactUs : NavRoute

    @Serializable
    data object PrivacyPolicy : NavRoute

    @Serializable
    data object TermsOfUse : NavRoute

    @Serializable
    data object Error : NavRoute
}