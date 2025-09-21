package com.devansh.noteapp.core.designsystem

sealed class OrderType {
    data object Ascending : OrderType()
    data object Descending : OrderType()
}