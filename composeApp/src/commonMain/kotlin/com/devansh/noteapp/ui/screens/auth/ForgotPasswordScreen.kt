package com.devansh.noteapp.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.button.BackButton
import com.devansh.noteapp.ui.utils.UiState
import io.github.jan.supabase.compose.auth.ui.annotations.AuthUiExperimental
import io.github.jan.supabase.compose.auth.ui.email.OutlinedEmailField
import org.koin.compose.viewmodel.koinViewModel

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

@OptIn(ExperimentalMaterial3Api::class, AuthUiExperimental::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    onSuccess: UnitCBF,
    onNavBack: UnitCBF
) {
    LaunchedEffect(Unit) {
        viewModel.setAuthMode(AuthMode.FORGOT_PASSWORD)
    }

    val email by viewModel.email.collectAsState()
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is UiState.Success) {
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Forgot Password") },
                navigationIcon = { BackButton(onClick = onNavBack) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            OutlinedEmailField(
                modifier = Modifier.widthIn(540.dp, Dp.Infinity),
                value = email,
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    autoCorrectEnabled = true,
                    imeAction = ImeAction.Next
                ),
                onValueChange = viewModel::onEmailChange,
                placeholder = { Text("abc@gmail.com") },
                label = { Text("Email") },
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.resetPassword() },
                enabled = authState !is UiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (authState is UiState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Text("Continue")
                }
            }
        }
    }
}