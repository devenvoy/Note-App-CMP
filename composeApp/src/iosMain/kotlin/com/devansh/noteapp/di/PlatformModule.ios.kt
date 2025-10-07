package com.devansh.noteapp.di

import com.devansh.noteapp.core.database.DatabaseDriverFactory
import com.devansh.noteapp.data.repository.AppCacheSetting
import com.devansh.noteapp.data.repository.IosSettingBuilder
import com.devansh.noteapp.data.repository.preference.AppCacheSettingImpl
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory() }
    single { IosSettingBuilder().createSettings() }
    single<AppCacheSetting> { AppCacheSettingImpl(get()) }
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