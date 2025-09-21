package com.devansh.noteapp.core.designsystem.utils

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.IntOffset

object ScreenTransitions {

    private val enterSpec = tween<IntOffset>(
        durationMillis = 800,
        easing = LinearOutSlowInEasing
    )

    private val exitSpec = tween<IntOffset>(
        durationMillis = 800,
        easing = FastOutLinearInEasing
    )

    val slideInFromBottom: EnterTransition = slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = enterSpec
    )

    val slideOutToBottom: ExitTransition = slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = exitSpec
    )
}
