package com.devansh.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.devansh.noteapp.data.local.DatabaseDriverFactory
import com.devansh.noteapp.data.local.SqlDelightNoteDataSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db =
            SqlDelightNoteDataSource(db = NoteDatabase(DatabaseDriverFactory(this).createDriver()))
        setContent {
            App(db)
        }
    }
}
