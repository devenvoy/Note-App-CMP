package com.devansh.noteapp.data.models.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("email") var email: String = "",
    @SerialName("emailVerified") var emailVerified: Boolean = false,
    @SerialName("id") var id: String = ""
)