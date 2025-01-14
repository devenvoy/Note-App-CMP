package com.devansh.noteapp.data.entity


import com.devansh.noteapp.data.remote.utils.Error
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class  ServerResponse<T>(
    @SerialName("statusCode")
    val code: Int?,
    @SerialName("data")
    val value: T? = null,
    @SerialName("message")
    val detail: String?,
    @SerialName("isSuccessful")
    val status: Boolean?
)

@Serializable
data class  ServerError(
    @SerialName("statusCode")
    val code: Int?,
    @SerialName("data")
    val value: Map<String, String>? = null,
    @SerialName("message")
    val detail: String?,
    @SerialName("isSuccessful")
    val status: Boolean? = false
): Error