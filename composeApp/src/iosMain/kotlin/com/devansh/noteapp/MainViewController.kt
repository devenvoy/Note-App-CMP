package com.devansh.noteapp

import androidx.compose.ui.window.ComposeUIViewController
import com.devansh.noteapp.di.platform_di.IosFirebaseRemoteConfig
import platform.UIKit.UIViewController

fun MainViewController(
    testConfig: Boolean
): UIViewController {

    IosFirebaseRemoteConfig.setIsForceUpdateEnabled(
        value = testConfig
    )
    return ComposeUIViewController { App() }
}