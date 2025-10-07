package com.devansh.noteapp

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalFoundationApi::class)
fun main() = application {
    val windowState = rememberWindowState(
        size = DpSize(420.dp, 840.dp), position = WindowPosition.Aligned(Alignment.Center)
    )
    val navController = rememberNavController()
    LaunchedEffect(Unit) {
        ComposeFoundationFlags.isNewContextMenuEnabled = true
    }
    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        alwaysOnTop = true,
        title = "My Notes "
    ) { App(navController) }
}