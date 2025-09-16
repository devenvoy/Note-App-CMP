package com.devansh.noteapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceSessionInfo(
    @SerialName("current") var current: Boolean = false,
    @SerialName("deviceId") var deviceId: String = "",
    @SerialName("deviceName") var deviceName: String = "",
    @SerialName("deviceType") var deviceType: String = "",
    @SerialName("ipAddress") var ipAddress: String = "",
    @SerialName("lastUsedAt") var lastUsedAt: String = ""
)