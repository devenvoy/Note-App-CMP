
package com.devansh.noteapp.data.models.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordWithTokenRequest(
    @SerialName("token") val token: String,
    @SerialName("newPassword") val newPassword: String
)