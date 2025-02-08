package com.devansh.noteapp.di.platform_di

import com.devansh.noteapp.NoteDatabase
import com.devansh.noteapp.data.local.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.UIKit.*
import platform.Foundation.*

actual fun platformModule(): Module = module {
    single { NoteDatabase(DatabaseDriverFactory().createDriver()) }
}

actual fun shareText(text: String, mimeType: String) {
    val activityController = UIActivityViewController(activityItems = listOf(text), applicationActivities = null)

    val controller = getCurrentViewController() // Implement a function to get top UIViewController
    controller?.presentViewController(activityController, animated = true, completion = null)
}

fun getCurrentViewController(): UIViewController? {
    val keyWindow = UIApplication.sharedApplication.keyWindow ?: return null
    var topController = keyWindow.rootViewController
    while (topController?.presentedViewController != null) {
        topController = topController.presentedViewController
    }
    return topController
}

actual fun isDesktop(): Boolean = false