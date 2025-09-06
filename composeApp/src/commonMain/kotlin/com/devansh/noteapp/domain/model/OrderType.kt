package com.devansh.noteapp.domain.model

sealed class OrderType {
    data object Ascending : OrderType()
    data object Descending : OrderType()
}