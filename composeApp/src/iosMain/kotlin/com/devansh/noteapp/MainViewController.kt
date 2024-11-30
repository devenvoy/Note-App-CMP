package com.devansh.noteapp

import androidx.compose.ui.window.ComposeUIViewController
import com.devansh.noteapp.data.local.DatabaseDriverFactory
import com.devansh.noteapp.data.local.SqlDelightNoteDataSource

fun MainViewController() = ComposeUIViewController {
    val db = SqlDelightNoteDataSource(NoteDatabase(DatabaseDriverFactory().createDriver()))
    App(db)
}