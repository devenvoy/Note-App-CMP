package com.maxkeppeker.sheets.core.utils

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun isLandscape(): Boolean {
    return when (calculateWindowSizeClass().widthSizeClass) {
        WindowWidthSizeClass.Expanded -> true
        else -> false
    }
}