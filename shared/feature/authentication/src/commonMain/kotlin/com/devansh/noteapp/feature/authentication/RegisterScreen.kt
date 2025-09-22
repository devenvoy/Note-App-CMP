package com.devansh.noteapp.feature.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.PredictiveBackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devansh.noteapp.core.designsystem.components.UiStateHandler
import com.devansh.noteapp.core.designsystem.components.button.BackButton
import com.devansh.noteapp.core.utils.DeviceConfiguration
import com.devansh.noteapp.core.utils.DeviceConfiguration.Companion.fromWindowSizeClass
import com.devansh.noteapp.core.utils.UnitCBF
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.coroutines.cancellation.CancellationException

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun RegisterScreenContent(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    onSuccess: UnitCBF,
    navigateBack: UnitCBF
) {
    LaunchedEffect(Unit) {
        authViewModel.setAuthMode(AuthMode.REGISTER)
    }
    val scope = rememberCoroutineScope()

    var playAnim by rememberSaveable { mutableFloatStateOf(0f) }
    var isClosing by rememberSaveable { mutableStateOf(false) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val screenHeight = LocalWindowInfo.current.containerSize.height
    val density = LocalDensity.current
    val screenHeightPx = with(density) { screenHeight.toDp().toPx() }

    val fillProgress by animateFloatAsState(
        targetValue = playAnim,
        animationSpec = tween(500, easing = LinearOutSlowInEasing),
        label = "fillProgress"
    )

    LaunchedEffect(playAnim) {
        if (isClosing.not()) {
            scope.launch {
                playAnim = 1f
            }
        }
    }

    PredictiveBackHandler(enabled = !isClosing) { progress ->
        try {
            progress.collect { backEvent ->
                playAnim = playAnim - (1F * backEvent.progress)
            }
            isClosing = true
            navigateBack()
        } catch (e: CancellationException) {
            throw e
        }
    }


    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {

        FillAnimationBox(fillProgress = fillProgress, startColor = Color.Transparent)

        if (fillProgress >= .5f) {
            Column(
                modifier = Modifier.align(Alignment.TopStart).padding(top = 100.dp, start = 20.dp)
            ) {
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
                .heightIn(min = 550.dp, max = 800.dp)
                .imePadding()
        }

        AnimatedVisibility(
            modifier = bottomModifier
                .graphicsLayer {
                    translationY = (translationY + offsetY)
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            if (offsetY > screenHeightPx / 3) {
                                navigateBack()
                            } else {
                                offsetY = 0f
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        val newOffset = offsetY + dragAmount.y
                        if (newOffset >= 0) {
                            offsetY = newOffset
                        }
                    }
                },
            visible = true,
            enter = slideInVertically { it } + fadeIn()
        ) {
            Column(
                modifier = contentModifier
                    .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BottomSheetDefaults.DragHandle()

                LoginScreenContent(viewModel = authViewModel, false)
            }
        }

        if (fillProgress >= .8f) {
            Box(modifier = Modifier.align(Alignment.BottomStart).padding(10.dp, 20.dp)) {
                BackButton(showBackText = true, text = "Back to Login") { navigateBack() }
            }
        }

        UiStateHandler(
            uiState = authViewModel.authState.collectAsState().value,
            onError = {},
            content = {},
            onSuccess = onSuccess
        )
    }
}