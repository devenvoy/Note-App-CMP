package com.devansh.noteapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("accessToken") var accessToken: String = "",
    @SerialName("refreshToken") var refreshToken: String = "",
    @SerialName("user") var user: User = User()
)

