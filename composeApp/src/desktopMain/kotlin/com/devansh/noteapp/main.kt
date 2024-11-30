package com.devansh.noteapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.devansh.noteapp.data.local.DatabaseDriverFactory
import com.devansh.noteapp.data.local.SqlDelightNoteDataSource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Note-App-CMP",
    ) {
        val db = SqlDelightNoteDataSource(NoteDatabase(DatabaseDriverFactory().createDriver()))
        App(db)
    }
}