package com.devansh.noteapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    val id: String,
    val name: String
)
