package com.devansh.noteapp.core.designsystem.components.formateToolbar

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class TypographyStyle(
    val name: String,
    val fontSize: TextUnit,
    val fontWeight: FontWeight,
    val description: String
){
    companion object{
        val typographyStyles = listOf(
            TypographyStyle("Title", 32.sp, FontWeight.Bold, "Large title text"),
            TypographyStyle("Subtitle", 28.sp, FontWeight.SemiBold, "Subtitle text"),
            TypographyStyle("Headline 1", 24.sp, FontWeight.Bold, "Main heading"),
            TypographyStyle("Headline 2", 20.sp, FontWeight.SemiBold, "Sub heading"),
            TypographyStyle("Headline 3", 18.sp, FontWeight.Medium, "Section heading"),
            TypographyStyle("Body Large", 16.sp, FontWeight.Normal, "Large body text"),
            TypographyStyle("Body", 14.sp, FontWeight.Normal, "Regular body text"),
            TypographyStyle("Caption", 12.sp, FontWeight.Normal, "Small caption text")
        )
    }
}