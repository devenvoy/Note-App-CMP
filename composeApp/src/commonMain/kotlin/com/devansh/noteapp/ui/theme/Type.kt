package com.devansh.noteapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import note_app_cmp.composeapp.generated.resources.Res
import note_app_cmp.composeapp.generated.resources.montserratbold
import note_app_cmp.composeapp.generated.resources.montserratregular
import note_app_cmp.composeapp.generated.resources.montserratsemibold
import note_app_cmp.composeapp.generated.resources.roboto_medium_numbers
import org.jetbrains.compose.resources.Font

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

@Composable
fun getMontRFont() = FontFamily(Font(Res.font.montserratregular))

@Composable
fun getMontBFont() = FontFamily(Font(Res.font.montserratbold))

@Composable
fun getMontSBFont() = FontFamily(Font(Res.font.montserratsemibold))

@Composable
fun getRobotoMFont() = FontFamily(Font(Res.font.roboto_medium_numbers))
