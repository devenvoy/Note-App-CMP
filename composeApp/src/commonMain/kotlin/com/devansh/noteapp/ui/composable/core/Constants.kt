package com.devansh.noteapp.ui.composable.core

import com.devansh.noteapp.data.local.SqlDelightNoteDataSource

class Constants{
    companion object{
        lateinit var dbClient: SqlDelightNoteDataSource
    }
}