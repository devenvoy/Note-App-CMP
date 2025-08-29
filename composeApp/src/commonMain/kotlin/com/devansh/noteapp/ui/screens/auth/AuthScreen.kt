package com.devansh.noteapp.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.PrimaryButton
import com.devansh.noteapp.ui.components.UiStateHandler
import io.github.jan.supabase.compose.auth.ui.annotations.AuthUiExperimental
import io.github.jan.supabase.compose.auth.ui.email.OutlinedEmailField
import io.github.jan.supabase.compose.auth.ui.password.OutlinedPasswordField
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.authScreen(navHostController: NavHostController) {
    composable<NavRoute.Auth> {
    AuthScreenContent() {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreenContent(onSuccess: () -> Unit) {
    val scope = rememberCoroutineScope()
    val authViewModel = koinViewModel<AuthScreenModel>()
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Authentication") }) }
    ) { sPad ->
        Box(modifier = Modifier.padding(sPad).fillMaxSize()) {

            Column {
                AuthTabs(
                    selectedTabIndex = selectedTabIndex,
                    onClick = { ix -> selectedTabIndex = ix },
                    tabs = listOf("Login", "Register")
                )

                LoginScreenContent(
                    viewModel = authViewModel,
                    isLogin = selectedTabIndex == 0
                )
            }

            UiStateHandler(
                uiState = authViewModel.authState.collectAsState().value,
                onErrorShowed = {},
                content = { onSuccess() }
            )
        }
    }
}

@Composable
fun AuthTabs(
    selectedTabIndex: Int,
    onClick: (Int) -> Unit,
    tabs: List<String>
) {
    SecondaryTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onClick(index) },
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = tab)
            }
        }
    }
}

@OptIn(AuthUiExperimental::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(viewModel: AuthScreenModel, isLogin: Boolean) {

    val email by viewModel.registerEmail.collectAsState()
    val password by viewModel.registerPassword.collectAsState()
    val confirmPwd by viewModel.registerConfirmPwd.collectAsState()

    Column(
        modifier = Modifier.padding(top = 20.dp).padding(8.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedEmailField(
            value = email,
            onValueChange = viewModel::onRegisterEmailChange,
            placeholder = { Text("Enter email") },
            label = { Text("Email") },
        )

        OutlinedPasswordField(
            value = password,
            onValueChange = viewModel::onRegisterPasswordChange,
            placeholder = { Text("Password") },
            label = { Text("Password") },
        )

        AnimatedVisibility(!isLogin) {
            OutlinedPasswordField(
                value = confirmPwd,
                onValueChange = viewModel::onRegisterConfirmPasswordChange,
                placeholder = { Text("confirm Password") },
                label = { Text("Confirm Password") },
            )
        }

        PrimaryButton(
            modifier = Modifier
                .widthIn(max = 300.dp, min = Dp.Infinity)
                .padding(top = 20.dp),
            onClick = {
                if (isLogin) {
                    viewModel.register()
                } else {
                    viewModel.login()
                }
            },
        ) {
            Text(
                text = if (isLogin) "Login" else "Register",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp
            )
        }
    }
}