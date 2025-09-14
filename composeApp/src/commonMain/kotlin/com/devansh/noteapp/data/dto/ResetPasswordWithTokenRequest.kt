
package com.devansh.noteapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordWithTokenRequest(
    @SerialName("token") val token: String,
    @SerialName("newPassword") val newPassword: String
)