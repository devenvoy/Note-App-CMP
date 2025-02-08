package com.maxkeppeker.sheets.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun AndroidWindowSizeFix(content: @Composable () -> Unit) {
    // Fix for androidx.window crash prior 1.3.0
    CompositionLocalProvider(
        LocalContext provides (LocalContext.current.findActivity() ?: LocalContext.current)
    ) {
        content()
    }
}

internal tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}