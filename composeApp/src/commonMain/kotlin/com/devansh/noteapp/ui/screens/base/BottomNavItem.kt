package com.devansh.noteapp.ui.screens.base

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val defaultIcon: ImageVector,
    val selectedIcon: ImageVector = defaultIcon,
    val route: Any
)
