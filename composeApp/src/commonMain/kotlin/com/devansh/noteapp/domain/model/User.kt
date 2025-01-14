package com.devansh.noteapp.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("userId")
    val userId: String?
)