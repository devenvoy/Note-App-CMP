package com.devansh.noteapp

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.compose.rememberNavController
import kotlinx.browser.document
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalBrowserHistoryApi::class
)
fun main() {
    onWasmReady {
        ComposeViewport(document.body!!) {
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                ComposeFoundationFlags.isNewContextMenuEnabled = true
//                navController.bindToBrowserNavigation()
            }
            App(navController)
        }
    }
}