package com.devansh.noteapp.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("authToken")
    val authToken: String,
    @SerialName("user")
    val user: User
)