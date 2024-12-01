package com.devansh.noteapp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.devansh.noteapp.data.local.SqlDelightNoteDataSource
import com.devansh.noteapp.ui.composable.core.Constants
import com.devansh.noteapp.ui.composable.splash.SplashScreen
import com.devansh.noteapp.ui.theme.NoteAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(db: SqlDelightNoteDataSource) {
    Constants.noteDatabase = db
  NoteAppTheme {
      Navigator(SplashScreen())
  }
}