
package com.devansh.noteapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceSessionInfo(
    @SerialName("deviceId") val deviceId: String,
    @SerialName("ipAddress") val ipAddress: String,
    @SerialName("userAgent") val userAgent: String,
    @SerialName("lastActive") val lastActive: String
)