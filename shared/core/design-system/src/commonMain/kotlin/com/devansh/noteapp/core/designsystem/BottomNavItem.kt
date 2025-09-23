package com.devansh.noteapp.core.designsystem

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

data class BottomNavItem(
    val title: StringResource,
    val defaultIcon: ImageVector,
    val selectedIcon: ImageVector = defaultIcon,
    val route: Any
)