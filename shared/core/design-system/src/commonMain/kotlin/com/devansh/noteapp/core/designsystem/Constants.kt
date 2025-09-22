package com.devansh.noteapp.core.designsystem

import androidx.compose.ui.graphics.Color
import com.devansh.noteapp.core.designsystem.theme.Chocolate
import com.devansh.noteapp.core.designsystem.theme.Coral
import com.devansh.noteapp.core.designsystem.theme.CrimsonRed
import com.devansh.noteapp.core.designsystem.theme.DarkOrchid
import com.devansh.noteapp.core.designsystem.theme.DeepPink
import com.devansh.noteapp.core.designsystem.theme.DodgerBlue
import com.devansh.noteapp.core.designsystem.theme.ForestGreen
import com.devansh.noteapp.core.designsystem.theme.Goldenrod
import com.devansh.noteapp.core.designsystem.theme.MidnightBlue
import com.devansh.noteapp.core.designsystem.theme.RoyalPurple
import com.devansh.noteapp.core.designsystem.theme.SlateGray
import com.devansh.noteapp.core.designsystem.theme.Teal

val textColors = listOf(
    Color.Black,
    Color.White,
    MidnightBlue,
    CrimsonRed,
    RoyalPurple,
    ForestGreen,
    Goldenrod,
    SlateGray,
    Coral,
    DarkOrchid,
    Teal,
    DeepPink,
    DodgerBlue,
    Chocolate
)

val urlRegex = Regex(
    "^((http|https)://)(www\\.)?" +
            "[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\." +
            "[a-z]{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)$"
)