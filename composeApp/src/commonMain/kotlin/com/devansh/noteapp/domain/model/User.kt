package com.devansh.noteapp.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("email") var email: String = "",
    @SerialName("emailVerified") var emailVerified: Boolean = false,
    @SerialName("id") var id: String = ""
)