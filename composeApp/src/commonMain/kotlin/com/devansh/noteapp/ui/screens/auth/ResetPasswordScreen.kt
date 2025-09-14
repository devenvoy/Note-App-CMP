package com.devansh.noteapp.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.button.BackButton
import com.devansh.noteapp.ui.utils.UiState
import io.github.jan.supabase.compose.auth.ui.annotations.AuthUiExperimental
import io.github.jan.supabase.compose.auth.ui.password.OutlinedPasswordField
import org.koin.compose.viewmodel.koinViewModel

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

@OptIn(AuthUiExperimental::class, ExperimentalMaterial3Api::class)
@Composable
private fun ResetPasswordScreen(
    viewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    onNavigateToLogin: UnitCBF,
) {
    LaunchedEffect(Unit) {
        viewModel.setAuthMode(AuthMode.RESET_PASSWORD)
    }

    val newPassword by viewModel.newPassword.collectAsState()
    val confirmNewPassword by viewModel.confirmNewPassword.collectAsState()
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is UiState.Success) {
            onNavigateToLogin()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reset Password") },
                navigationIcon = { BackButton(onNavigateToLogin) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            OutlinedPasswordField(
                value = newPassword,
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next
                ),
                onValueChange = viewModel::onNewPasswordChange,
                placeholder = { Text("Stasp78JK") },
                label = { Text("New Password") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedPasswordField(
                value = confirmNewPassword,
                onValueChange = viewModel::onConfirmNewPasswordChange,
                label = { Text("Confirm New Password") },
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done
                ),
                placeholder = { Text("Stasp78JK") },
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
                    Text("Reset Password")
                }
            }
        }
    }
}