
package com.devansh.noteapp.data.models.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    @SerialName("email") val email: String,
    @SerialName("otp") val otp: String
)