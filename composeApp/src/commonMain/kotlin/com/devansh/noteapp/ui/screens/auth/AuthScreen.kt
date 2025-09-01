package com.devansh.noteapp.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.core.util.DeviceConfiguration.Companion.fromWindowSizeClass
import com.devansh.noteapp.core.util.DeviceConfiguration.DESKTOP
import com.devansh.noteapp.core.util.DeviceConfiguration.MOBILE_LANDSCAPE
import com.devansh.noteapp.core.util.DeviceConfiguration.MOBILE_PORTRAIT
import com.devansh.noteapp.core.util.DeviceConfiguration.TABLET_LANDSCAPE
import com.devansh.noteapp.core.util.DeviceConfiguration.TABLET_PORTRAIT
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.PrimaryButton
import com.devansh.noteapp.ui.components.UiStateHandler
import io.github.jan.supabase.compose.auth.ui.annotations.AuthUiExperimental
import io.github.jan.supabase.compose.auth.ui.email.OutlinedEmailField
import io.github.jan.supabase.compose.auth.ui.password.OutlinedPasswordField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.authScreen(navHostController: NavHostController) {
    composable<NavRoute.Auth> {
        AuthScreenContent {
            navHostController.navigate(NavRoute.HomeScreen)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuthScreenContent(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    onSuccess: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    var showLoader by rememberSaveable { mutableStateOf(true) }
    var showFill by rememberSaveable { mutableStateOf(false) }
    var showContent by rememberSaveable { mutableStateOf(false) }

    var playAnim by remember { mutableStateOf(false) }
    val fillProgress by animateFloatAsState(
        targetValue = if (playAnim) 1f else 0f,
        animationSpec = tween(1000, easing = LinearEasing),
        label = "fillProgress"
    )

    LaunchedEffect(Unit) {
        scope.launch {
            delay(750)
            showFill = true
            playAnim = true
        }

        delay(1000)
        showLoader = false
        showContent = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(visible = showLoader) {
            ContainedLoadingIndicator(
                modifier = Modifier.size(100.dp),
                containerColor = Color.Transparent,
                indicatorColor = MaterialTheme.colorScheme.primary,
            )
        }

        if (showFill) {
            FillAnimationBox(fillProgress = fillProgress)
        }

        AnimatedVisibility(
            visible = selectedTabIndex == 0,
            enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut() + slideOutVertically { -it },
            modifier = Modifier.align(Alignment.TopStart).padding(top = 100.dp, start = 20.dp)
        ) {
            Column {
                Text(
                    text = "Let's Login",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 42.sp),
                    color = MaterialTheme.colorScheme.background,
                )
                Text(
                    text = "And, keep safe ideas",
                    color = MaterialTheme.colorScheme.background,
                )
            }
        }

        AnimatedVisibility(
            visible = selectedTabIndex == 1,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it },
            modifier = Modifier.align(Alignment.TopStart).padding(top = 100.dp, start = 20.dp)
        ) {
            Column {
                Text(
                    text = "Register Here",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 42.sp),
                    color = MaterialTheme.colorScheme.background,
                )
                Text(
                    text = "Join us and start noting.",
                    color = MaterialTheme.colorScheme.background,
                )
            }
        }

        val windowInfo = currentWindowAdaptiveInfo()
        val deviceConfiguration = fromWindowSizeClass(windowInfo.windowSizeClass)

        val bottomModifier = when (deviceConfiguration) {
            MOBILE_PORTRAIT,
            TABLET_PORTRAIT,
            DESKTOP -> Modifier.align(Alignment.BottomCenter)

            MOBILE_LANDSCAPE,
            TABLET_LANDSCAPE -> Modifier.align(Alignment.BottomEnd)
                .statusBarsPadding()
                .padding(end = 20.dp)
        }

        val contentModifier = when (deviceConfiguration) {
            MOBILE_LANDSCAPE, TABLET_LANDSCAPE -> Modifier
                .widthIn(max = 500.dp)
                .heightIn(min = 200.dp, max = 300.dp)
                .verticalScroll(rememberScrollState())

            else -> Modifier
                .widthIn(max = 500.dp)
                .heightIn(min = 600.dp, max = 800.dp)
                .imePadding()
        }

        AnimatedVisibility(
            modifier = bottomModifier,
            visible = showContent,
            enter = slideInVertically { it } + fadeIn()
        ) {
            Column(
                modifier = contentModifier
                    .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BottomSheetDefaults.DragHandle()

                AuthTabs(
                    selectedTabIndex = selectedTabIndex,
                    onClick = { ix -> selectedTabIndex = ix },
                    tabs = listOf("Login", "Register")
                )

                LoginScreenContent(
                    viewModel = authViewModel, isLogin = selectedTabIndex == 0
                )
            }
        }

        UiStateHandler(
            uiState = authViewModel.authState.collectAsState().value,
            onErrorShowed = {},
            content = { onSuccess() }
        )
    }
}


@Composable
fun AuthTabs(
    selectedTabIndex: Int, onClick: (Int) -> Unit, tabs: List<String>
) {
    SecondaryTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onClick(index) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = tab)
            }
        }
    }
}

@OptIn(AuthUiExperimental::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(viewModel: AuthViewModel, isLogin: Boolean) {

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPwd by viewModel.confirmPassword.collectAsState()

    val inputModifier = Modifier.fillMaxWidth(.9f)

    Column(
        modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedEmailField(
            modifier = inputModifier,
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

        OutlinedPasswordField(
            modifier = inputModifier,
            value = password,
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrectEnabled = false,
                imeAction = if (isLogin) ImeAction.Done else ImeAction.Next
            ),
            onValueChange = viewModel::onPasswordChange,
            placeholder = { Text("Stasp78JK") },
            label = { Text("Password") },
        )

        AnimatedVisibility(!isLogin) {
            OutlinedPasswordField(
                modifier = inputModifier,
                value = confirmPwd,
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done
                ),
                onValueChange = viewModel::onConfirmPasswordChanged,
                placeholder = { Text("Stasp78JK") },
                label = { Text("Confirm Password") },
            )
        }

        PrimaryButton(
            modifier = Modifier.widthIn(max = 300.dp, min = Dp.Infinity).padding(vertical = 20.dp),
            onClick = {
                if (isLogin) {
                    viewModel.login()
                } else {
                    viewModel.register()
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

@Composable
fun BoxScope.FillAnimationBox(fillProgress: Float) {
    val radiusPx = with(LocalDensity.current) {
        (fillProgress * 2000).dp.toPx()
    }.coerceAtLeast(1f)

    Box(
        modifier = Modifier.matchParentSize()
            .background(
                Brush.radialGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    MaterialTheme.colorScheme.background
                ), center = Offset.Unspecified, radius = radiusPx
            )
        )
    )
}
