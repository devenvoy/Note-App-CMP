package com.devansh.noteapp.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.devansh.noteapp.domain.utils.koinScreenModel
import com.devansh.noteapp.ui.components.CustomInputField
import com.devansh.noteapp.ui.components.CustomInputPasswordField
import com.devansh.noteapp.ui.components.PrimaryButton
import com.devansh.noteapp.ui.components.UiStateHandler
import com.devansh.noteapp.ui.screens.home.HomeScreen
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Envelope
import kotlinx.coroutines.launch
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

class AuthScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        AuthScreenContent(
            onSuccess= {
                navigator.replace(HomeScreen())
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AuthScreenContent(onSuccess: () -> Unit) {
        val scope = rememberCoroutineScope()
        val tabs by remember { mutableStateOf(listOf(TabItem.Login, TabItem.Register)) }
        val pagerState = rememberPagerState(pageCount = { tabs.size })
        val authViewModel = koinScreenModel<AuthScreenModel>()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Authentication")
                    }
                )
            }
        ) { sPad ->
            Box(modifier = Modifier.padding(sPad).fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Column {
                    Tabs(tabs = tabs,
                        pagerState = pagerState,
                        onClick = { scope.launch { pagerState.animateScrollToPage(it) } })
                    TabsContent(
                        modifier = Modifier.weight(1f),
                        authViewModel = authViewModel,
                        tabs = tabs,
                        pagerState = pagerState,
                    )
                }
                UiStateHandler(
                    uiState = authViewModel.authState.collectAsState().value,
                    onErrorShowed = {},
                    content = {
                        onSuccess()
                    }
                )
            }
        }
    }


    @Composable
    fun TabsContent(
        modifier: Modifier = Modifier,
        pagerState: PagerState,
        tabs: List<TabItem>,
        authViewModel: AuthScreenModel,
    ) {
        HorizontalPager(modifier = modifier, state = pagerState) { page ->
            tabs[page].screen(authViewModel)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Tabs(
        pagerState: PagerState,
        onClick: (Int) -> Unit,
        tabs: List<TabItem>
    ) {
        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { onClick(index) },
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(text = tab.title)
                }
            }
        }
    }


    sealed class TabItem(
        var icon: Int,
        var title: String,
        var screen: @Composable (AuthScreenModel) -> Unit
    ) {
        data object Login : TabItem(0, "Login", { viewModel ->
            loginScreenContent(viewModel)
        })

        data object Register : TabItem(0, "Register", { viewModel ->
            registerScreenContent(viewModel)
        })
    }
}

@Composable
fun loginScreenContent(viewModel: AuthScreenModel) {

    val email by viewModel.loginEmail.collectAsState()
    val password by viewModel.loginPassword.collectAsState()
    Column(
        modifier = Modifier.padding(top = 20.sdp).padding(8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        Column {
            CustomInputField(
                fieldTitle = "Email",
                textFieldValue = email,
                onValueChange = viewModel::onLoginEmailChange,
                placeholder = { Text("Enter email") },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.size(20.sdp),
                        imageVector = FontAwesomeIcons.Regular.Envelope,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Email icon",
                    )
                },
            )
            Spacer(modifier = Modifier.height(10.sdp))
            CustomInputPasswordField(
                fieldTitle = "Password",
                textFieldValue = password,
                onValueChange = viewModel::onLoginPasswordChange,
                placeholder = { Text("Password") },
                isPasswordField = true
            )
        }
        Spacer(modifier = Modifier.height(20.sdp))
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.login() },
        ) {
            Text(
                text = "Sign In",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.ssp
            )
        }
    }
}

@Composable
fun registerScreenContent(viewModel: AuthScreenModel) {

    val email by viewModel.registerEmail.collectAsState()
    val password by viewModel.registerPassword.collectAsState()
    val confirmPwd by viewModel.registerConfirmPwd.collectAsState()
    Column(
        modifier = Modifier.padding(top = 20.sdp).padding(8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        Column {
            CustomInputField(
                fieldTitle = "Email",
                textFieldValue = email,
                onValueChange = viewModel::onRegisterEmailChange,
                placeholder = { Text("Enter email") },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.size(20.sdp),
                        imageVector = FontAwesomeIcons.Regular.Envelope,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Email icon",
                    )
                },
            )
            Spacer(modifier = Modifier.height(10.sdp))
            CustomInputPasswordField(
                fieldTitle = "Password",
                textFieldValue = password,
                onValueChange = viewModel::onRegisterPasswordChange,
                placeholder = { Text("Password") },
                isPasswordField = true
            )
            Spacer(modifier = Modifier.height(10.sdp))
            CustomInputPasswordField(
                fieldTitle = "Confirm Password",
                textFieldValue = confirmPwd,
                onValueChange = viewModel::onRegisterConfirmPasswordChange,
                placeholder = { Text("confirm Password") },
                isPasswordField = true
            )
        }
        Spacer(modifier = Modifier.height(20.sdp))
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.register() },
        ) {
            Text(
                text = "Register",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.ssp
            )
        }
    }
}