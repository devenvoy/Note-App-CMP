package com.devansh.noteapp.di.platform_di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.devansh.noteapp.NoteAppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

actual fun platformModule(): Module = module {
    single<SqlDriver> {
        NativeSqliteDriver(NoteAppDatabase.Schema.synchronous(), "note_db.db")
    }
}

actual fun shareText(text: String, mimeType: String) {
    val activityController = UIActivityViewController(activityItems = listOf(text), applicationActivities = null)
    val controller = getCurrentViewController()
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