package com.devansh.noteapp.ui.screens.core

import androidx.compose.ui.graphics.Color
import com.devansh.noteapp.ui.theme.Chocolate
import com.devansh.noteapp.ui.theme.Coral
import com.devansh.noteapp.ui.theme.CrimsonRed
import com.devansh.noteapp.ui.theme.DarkOrchid
import com.devansh.noteapp.ui.theme.DeepPink
import com.devansh.noteapp.ui.theme.DodgerBlue
import com.devansh.noteapp.ui.theme.ForestGreen
import com.devansh.noteapp.ui.theme.Goldenrod
import com.devansh.noteapp.ui.theme.MidnightBlue
import com.devansh.noteapp.ui.theme.RoyalPurple
import com.devansh.noteapp.ui.theme.SlateGray
import com.devansh.noteapp.ui.theme.Teal

enum class ListType {
    GRID, LIST
}

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