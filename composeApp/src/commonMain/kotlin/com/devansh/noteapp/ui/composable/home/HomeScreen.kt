package com.devansh.noteapp.ui.composable.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        Text(text = "Home Screen", fontSize = 24.sp)
    }
}