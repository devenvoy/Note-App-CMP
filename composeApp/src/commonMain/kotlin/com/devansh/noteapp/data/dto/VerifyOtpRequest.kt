
package com.devansh.noteapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    @SerialName("email") val email: String,
    @SerialName("otp") val otp: String
)