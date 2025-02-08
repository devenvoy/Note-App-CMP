package com.maxkeppeker.sheets.core.utils

import androidx.compose.runtime.Composable

@Composable
actual fun AndroidWindowSizeFix(content: @Composable () -> Unit) {
    content()
}