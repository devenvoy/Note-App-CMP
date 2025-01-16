package com.devansh.noteapp.di

import com.devansh.noteapp.data.local.NoteDataSourceImpl
import com.devansh.noteapp.data.preference.AppCacheSettingImpl
import com.devansh.noteapp.data.remote.AuthDaoImpl
import com.devansh.noteapp.data.remote.NoteRemoteDaoImpl
import com.devansh.noteapp.di.platform_di.getHttpClient
import com.devansh.noteapp.di.platform_di.platformModule
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.AuthDao
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.repo.NoteRemoteDao
import com.devansh.noteapp.ui.screens.add_edit_note.AddEditNoteViewModel
import com.devansh.noteapp.ui.screens.auth.AuthScreenModel
import com.devansh.noteapp.ui.screens.home.HomeScreenModel
import com.devansh.noteapp.ui.screens.splash.SplashScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val screenModelsModule = module {
    factory { SplashScreenModel(get(),get(),get()) }
    factory { AuthScreenModel(get(),get()) }
    factory { HomeScreenModel(get(),get(),get()) }
    factory { AddEditNoteViewModel(get()) }
}

val repositoryModule = module {
    single<NoteDataSource> { NoteDataSourceImpl(get(), get()) }
    single<NoteRemoteDao> { NoteRemoteDaoImpl(get()) }
    single<AuthDao> { AuthDaoImpl(get()) }
}

val dispatcherModule = module {
    single { Dispatchers.IO }
    single { Dispatchers.Default }
    single { Dispatchers.Main }
    single { Dispatchers.Unconfined }
}

val dataModule = module {
    single { getHttpClient() }
    single<AppCacheSetting> { AppCacheSettingImpl() }
}

val appModules = listOf(
    platformModule(),  // for database
    dataModule,
    repositoryModule,
    screenModelsModule,
    dispatcherModule,
)