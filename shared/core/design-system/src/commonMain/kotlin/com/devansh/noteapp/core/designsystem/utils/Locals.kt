package com.devansh.noteapp.core.designsystem.utils

import androidx.compose.runtime.staticCompositionLocalOf
import com.devansh.noteapp.core.utils.DeviceConfiguration

val LocalDeviceConfiguration =
    staticCompositionLocalOf<DeviceConfiguration> { error("No Configuration Provided") }