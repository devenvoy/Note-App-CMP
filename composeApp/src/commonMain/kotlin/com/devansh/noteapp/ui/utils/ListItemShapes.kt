package com.devansh.noteapp.ui.utils

import androidx.compose.foundation.shape.RoundedCornerShape

object ListItemShapes {

    private const val LargeCornerRadiusToken = 25
    private const val SmallCornerRadiusToken = 4

    val firstIndexShape = RoundedCornerShape(LargeCornerRadiusToken, LargeCornerRadiusToken, SmallCornerRadiusToken, SmallCornerRadiusToken)
    val middleIndexShape = RoundedCornerShape(SmallCornerRadiusToken, SmallCornerRadiusToken, SmallCornerRadiusToken, SmallCornerRadiusToken)
    val lastIndexShape = RoundedCornerShape(SmallCornerRadiusToken, SmallCornerRadiusToken, LargeCornerRadiusToken, LargeCornerRadiusToken)
    val singleItemIndex = RoundedCornerShape(LargeCornerRadiusToken, LargeCornerRadiusToken, LargeCornerRadiusToken, LargeCornerRadiusToken)
}