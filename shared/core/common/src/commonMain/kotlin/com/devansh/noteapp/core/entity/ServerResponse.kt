package com.devansh.noteapp.core.entity


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class  ServerResponse<T>(
    @SerialName("statusCode") val code: Int = 200,
    @SerialName("data") val value: T? = null,
    @SerialName("message") val detail: String?,
    @SerialName("isSuccessful") val status: Boolean = true
)

@Serializable
data class  ServerError(
    @SerialName("code") val code: Int?,
    @SerialName("message") val detail: String?,
    @SerialName("status") val status: Boolean = false,
    @SerialName("data") val value: Map<String, String>? = null
): com.devansh.noteapp.core.entity.Error
