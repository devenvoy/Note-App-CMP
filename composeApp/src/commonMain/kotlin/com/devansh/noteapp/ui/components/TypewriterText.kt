package com.devansh.noteapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun TypewriterText(
    text: String,
    style: TextStyle = TextStyle(
        fontSize = 12.ssp,
        color = Color.DarkGray
    )
) {
    var textToDisplay by remember { mutableStateOf("") }
    val words: List<String> = remember { text.split(" ") }

    LaunchedEffect(text) {
        for (word in words) {
            for (char in word) {
                textToDisplay += char
                delay(80)
            }
            textToDisplay += " "
            delay(240)
        }
    }

    Text(
        text = textToDisplay,
        style = style
    )
}
