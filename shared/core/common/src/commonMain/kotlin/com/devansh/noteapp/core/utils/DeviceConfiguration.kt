package com.devansh.noteapp.core.utils

import androidx.window.core.layout.WindowSizeClass

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            val isWidthCompact = !windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND)
            val isWidthMedium = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND) &&
                    !windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND)
            val isWidthExpanded = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND)

            val isHeightCompact = !windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND)
            val isHeightMedium = windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND) &&
                    !windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND)
            val isHeightExpanded = windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND)

            return when {
                isWidthCompact && (isHeightMedium || isHeightExpanded) -> MOBILE_PORTRAIT
                isWidthExpanded && isHeightCompact -> MOBILE_LANDSCAPE
                isWidthMedium && isHeightExpanded -> TABLET_PORTRAIT
                isWidthExpanded && isHeightMedium -> TABLET_LANDSCAPE
                else -> DESKTOP
            }
        }
    }
    fun isMobile() = this == MOBILE_PORTRAIT || this == MOBILE_LANDSCAPE
    fun isMobilePortrait() = this == MOBILE_PORTRAIT
    fun isMobileLandscape() = this == MOBILE_LANDSCAPE
    fun isTablet() = this == TABLET_PORTRAIT || this == TABLET_LANDSCAPE
    fun isTabletPortrait() = this == TABLET_PORTRAIT
    fun isTabletLandscape() = this == TABLET_LANDSCAPE
    fun isDesktop() = this == DESKTOP
    fun isPortrait() = this == MOBILE_PORTRAIT || this == TABLET_PORTRAIT
    fun isLandscape() = this == MOBILE_LANDSCAPE || this == TABLET_LANDSCAPE
    fun isCompact() = this == MOBILE_PORTRAIT || this == MOBILE_LANDSCAPE
    fun isExpanded() = this == TABLET_PORTRAIT || this == TABLET_LANDSCAPE || this == DESKTOP
}