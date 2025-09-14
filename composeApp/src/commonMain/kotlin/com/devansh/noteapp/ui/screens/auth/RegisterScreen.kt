package com.devansh.noteapp.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ContainedLoadingIndicator
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.core.util.DeviceConfiguration
import com.devansh.noteapp.core.util.DeviceConfiguration.Companion.fromWindowSizeClass
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.UiStateHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.registerScreen(
    mainNavController: NavHostController
) {
    composable<NavRoute.Register> {
        RegisterScreenContent(onSuccess = {
            mainNavController.navigate(NavRoute.Login) {
                popUpTo<NavRoute.Register> { inclusive = true }
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun RegisterScreenContent(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    onSuccess: () -> Unit
) {
    LaunchedEffect(Unit) {
        authViewModel.setAuthMode(AuthMode.REGISTER)
    }
    val scope = rememberCoroutineScope()
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


        val windowInfo = currentWindowAdaptiveInfo()
        val deviceConfiguration = fromWindowSizeClass(windowInfo.windowSizeClass)

        val bottomModifier = when (deviceConfiguration) {
            DeviceConfiguration.MOBILE_PORTRAIT,
            DeviceConfiguration.TABLET_PORTRAIT,
            DeviceConfiguration.DESKTOP -> Modifier.align(Alignment.BottomCenter)

            DeviceConfiguration.MOBILE_LANDSCAPE,
            DeviceConfiguration.TABLET_LANDSCAPE -> Modifier.align(Alignment.BottomEnd)
                .statusBarsPadding()
                .padding(end = 20.dp)
        }

        val contentModifier = when (deviceConfiguration) {
            DeviceConfiguration.MOBILE_LANDSCAPE, DeviceConfiguration.TABLET_LANDSCAPE -> Modifier
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

                LoginScreenContent(viewModel = authViewModel, true)
            }
        }

        UiStateHandler(
            uiState = authViewModel.authState.collectAsState().value,
            onErrorShowed = {},
            content = { onSuccess() }
        )
    }
}