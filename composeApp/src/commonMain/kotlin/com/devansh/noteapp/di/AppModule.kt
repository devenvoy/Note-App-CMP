package com.devansh.noteapp.di

import com.devansh.noteapp.data.local.NoteDataSourceImpl
import com.devansh.noteapp.di.platform_di.getHttpClient
import com.devansh.noteapp.di.platform_di.platformModule
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.ui.screens.add_edit_note.AddEditNoteViewModel
import com.devansh.noteapp.ui.screens.home.HomeScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val screenModelsModule = module {
    factory { AddEditNoteViewModel(get()) }
    factory { HomeScreenModel(get()) }
}


val repositoryModule = module {
    single<NoteDataSource> {
        NoteDataSourceImpl(get(), get())
    }
}

val dispatcherModule = module {
    single { Dispatchers.IO }
    single { Dispatchers.Default }
    single { Dispatchers.Main }
    single { Dispatchers.Unconfined }
}

val dataModule = module {
    single { getHttpClient() }
}

val appModules = listOf(
    platformModule(),  // for database
    dataModule,
    repositoryModule,
    screenModelsModule,
    dispatcherModule,
)