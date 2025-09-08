package com.devansh.noteapp.domain.entity


import com.devansh.noteapp.domain.utils.Error
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class  ServerResponse<T>(
    @SerialName("statusCode") val code: Int?,
    @SerialName("data") val value: T? = null,
    @SerialName("message") val detail: String?,
    @SerialName("isSuccessful") val status: Boolean = false
)

@Serializable
data class  ServerError(
    @SerialName("code") val code: Int?,
    @SerialName("message") val detail: String?,
    @SerialName("status") val status: Boolean = false,
    @SerialName("data") val value: Map<String, String>? = null
): Error
