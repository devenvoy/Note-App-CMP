package com.devansh.noteapp.feature.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devansh.noteapp.core.designsystem.components.UiStateHandler
import com.devansh.noteapp.core.designsystem.components.button.PrimaryButton
import com.devansh.noteapp.core.utils.DeviceConfiguration.Companion.fromWindowSizeClass
import com.devansh.noteapp.core.utils.DeviceConfiguration.DESKTOP
import com.devansh.noteapp.core.utils.DeviceConfiguration.MOBILE_LANDSCAPE
import com.devansh.noteapp.core.utils.DeviceConfiguration.MOBILE_PORTRAIT
import com.devansh.noteapp.core.utils.DeviceConfiguration.TABLET_LANDSCAPE
import com.devansh.noteapp.core.utils.DeviceConfiguration.TABLET_PORTRAIT
import com.devansh.noteapp.core.utils.UnitCBF
import io.github.jan.supabase.compose.auth.ui.annotations.AuthUiExperimental
import io.github.jan.supabase.compose.auth.ui.email.OutlinedEmailField
import io.github.jan.supabase.compose.auth.ui.password.OutlinedPasswordField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    onSuccess: UnitCBF,
    navToForgotPassword: UnitCBF,
    navToRegister: UnitCBF
) {
    LaunchedEffect(Unit) {
        authViewModel.setAuthMode(AuthMode.LOGIN)
    }
    val scope = rememberCoroutineScope()
    var showLoader by rememberSaveable { mutableStateOf(true) }
    var showFill by rememberSaveable { mutableStateOf(false) }
    var showContent by rememberSaveable { mutableStateOf(false) }

    var playAnim by rememberSaveable { mutableStateOf(false) }

    val fillProgress by animateFloatAsState(
        targetValue = if (playAnim) 1f else 0f,
        animationSpec = tween(1000, easing = LinearOutSlowInEasing),
        label = "fillProgress"
    )

    LaunchedEffect(Unit) {
        if (!showContent) {
            scope.launch {
                delay(750)
                showFill = true
                playAnim = true
            }
            delay(1000)
            showLoader = false
            showContent = true
        }
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

        Column(
            modifier = Modifier.align(Alignment.TopStart).padding(top = 100.dp, start = 20.dp)
        ) {
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
                .fillMaxWidth()
                .heightIn(min = 550.dp, max = 800.dp)
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

                LoginScreenContent(viewModel = authViewModel, true, navToForgotPassword)

                ElevatedButton(
                    modifier = Modifier.widthIn(max = 300.dp, min = Dp.Infinity),
                    onClick = navToRegister,
                    shapes = ButtonDefaults.shapes()
                ) {
                    Text(
                        buildAnnotatedString {
                            append("Don't have an account? ")
                            append("Register")
                        }
                    )
                }
            }
        }
        UiStateHandler(
            uiState = authViewModel.authState.collectAsState().value,
            onError = {},
            content = { },
            onSuccess = onSuccess
        )
    }
}

@OptIn(AuthUiExperimental::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    viewModel: AuthViewModel,
    isLogin: Boolean,
    navToForgotPassword: UnitCBF? = null
) {

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPwd by viewModel.confirmPassword.collectAsState()

    val inputModifier = Modifier.fillMaxWidth(.9f)

    Column(
        modifier = Modifier.padding(top = 20.dp)
            .fillMaxWidth(),
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
            rules = viewModel.passwordRules,
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
                rules = viewModel.passwordRules,
                onValueChange = viewModel::onConfirmPasswordChanged,
                placeholder = { Text("Stasp78JK") },
                label = { Text("Confirm Password") },
            )
        }

        AnimatedVisibility(isLogin, modifier = Modifier.align(Alignment.End)) {
            Text(
                modifier = Modifier.padding(end = 20.dp, top = 8.dp)
                    .clickable(onClick = navToForgotPassword!!),
                text = "Forgot Password?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        PrimaryButton(
            modifier = Modifier.widthIn(max = 300.dp, min = Dp.Infinity)
                .padding(vertical = 20.dp),
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
fun BoxScope.FillAnimationBox(fillProgress: Float, startColor: Color? = null) {
    val radiusPx = with(LocalDensity.current) {
        (fillProgress * 2000).dp.toPx()
    }.coerceAtLeast(1f)

    Box(
        modifier = Modifier.matchParentSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 1f),
                        (startColor ?: MaterialTheme.colorScheme.background)
                    ), center = Offset.Unspecified, radius = radiusPx
                )
            )
    )
}
