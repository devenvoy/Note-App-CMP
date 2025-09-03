package com.devansh.noteapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.devansh.noteapp.ui.components.EmptyScreen

@SuppressLint("StaticFieldLeak")
class MainActivity : ComponentActivity() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
        setContent { App() }
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun emptyScreenPreview() {
}