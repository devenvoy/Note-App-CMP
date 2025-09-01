package com.devansh.noteapp.di

import com.devansh.noteapp.data.local.NoteDataSourceImpl
import com.devansh.noteapp.data.preference.AppCacheSettingImpl
import com.devansh.noteapp.data.remote.AuthServiceImpl
import com.devansh.noteapp.data.remote.NoteRemoteServiceImpl
import com.devansh.noteapp.di.platform_di.getHttpClient
import com.devansh.noteapp.di.platform_di.platformModule
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.AuthService
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.repo.NoteRemoteService
import com.devansh.noteapp.ui.screens.add_edit_note.AddEditNoteViewModel
import com.devansh.noteapp.ui.screens.auth.AuthViewModel
import com.devansh.noteapp.ui.screens.home.HomeScreenViewModel
import com.devansh.noteapp.ui.screens.setting.SettingViewModel
import com.devansh.noteapp.ui.screens.splash.SplashScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val screenModelsModule = module {
    viewModel { SplashScreenViewModel(get(),get()) }
    viewModel { AuthViewModel(get(),get()) }
    viewModel { HomeScreenViewModel(get(),get(),get()) }
    viewModel { AddEditNoteViewModel(get()) }
    viewModel { SettingViewModel(get(), get()) }
}

val repositoryModule = module {
    single<NoteDataSource> { NoteDataSourceImpl(get(), get()) }
    single<NoteRemoteService> { NoteRemoteServiceImpl(get()) }
    single<AuthService> { AuthServiceImpl(get()) }
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
    platformModule(),
    dataModule,
    repositoryModule,
    screenModelsModule,
    dispatcherModule,
)