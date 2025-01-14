package com.devansh.noteapp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.devansh.noteapp.di.appModules
import com.devansh.noteapp.ui.screens.splash.SplashScreen
import com.devansh.noteapp.ui.theme.NoteAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
  NoteAppTheme {
      KoinApplication(
          application = {
              modules(appModules)
          }
      ) {
          Navigator(SplashScreen())
      }
  }
}