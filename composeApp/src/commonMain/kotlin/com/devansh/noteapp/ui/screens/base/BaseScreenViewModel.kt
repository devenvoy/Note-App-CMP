package com.devansh.noteapp.ui.screens.base

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.lifecycle.ViewModel
import com.devansh.noteapp.navigation.NavRoute
import noteapp.composeapp.generated.resources.Res
import noteapp.composeapp.generated.resources.category
import noteapp.composeapp.generated.resources.note

class BaseScreenViewModel() : ViewModel() {

    val bottomNavItems = listOf(
        BottomNavItem(
            title = Res.string.note,
            defaultIcon = Icons.AutoMirrored.Outlined.Feed,
            selectedIcon = Icons.AutoMirrored.Filled.Feed,
            route = NavRoute.HomeScreen,
        ),
        BottomNavItem(
            title = Res.string.category,
            defaultIcon = Icons.Filled.Folder,
            selectedIcon = Icons.Filled.FolderOpen,
            route = NavRoute.Category
        )
    )
}