package com.devansh.noteapp.di.platform_di

import com.devansh.noteapp.NoteDatabase
import com.devansh.noteapp.data.local.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { NoteDatabase(DatabaseDriverFactory().createDriver()) }
}