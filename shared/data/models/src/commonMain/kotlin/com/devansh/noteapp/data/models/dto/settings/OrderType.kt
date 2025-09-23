package com.devansh.noteapp.data.models.dto.settings

sealed class OrderType {
    data object Ascending : OrderType()
    data object Descending : OrderType()
}