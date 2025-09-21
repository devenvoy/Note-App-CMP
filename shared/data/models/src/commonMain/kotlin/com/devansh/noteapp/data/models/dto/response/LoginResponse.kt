package com.devansh.noteapp.data.models.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("accessToken") var accessToken: String = "",
    @SerialName("refreshToken") var refreshToken: String = "",
    @SerialName("user") var user: UserResponse = UserResponse()
)