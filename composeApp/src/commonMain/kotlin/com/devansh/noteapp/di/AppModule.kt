package com.devansh.noteapp.di

import com.devansh.noteapp.data.local.CategoryDataSourceImpl
import com.devansh.noteapp.data.local.NoteDataSourceImpl
import com.devansh.noteapp.data.preference.AppCacheSettingImpl
import com.devansh.noteapp.data.remote.AuthServiceImpl
import com.devansh.noteapp.data.remote.CategoryServiceImpl
import com.devansh.noteapp.data.remote.NoteServiceImpl
import com.devansh.noteapp.di.platform_di.getHttpClient
import com.devansh.noteapp.di.platform_di.platformModule
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.AuthService
import com.devansh.noteapp.domain.repo.CategoryDataSource
import com.devansh.noteapp.domain.repo.CategoryService
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.repo.NoteService
import com.devansh.noteapp.ui.screens.add_edit_note.AddEditNoteViewModel
import com.devansh.noteapp.ui.screens.auth.AuthViewModel
import com.devansh.noteapp.ui.screens.base.BaseScreenViewModel
import com.devansh.noteapp.ui.screens.categoryFolder.CategoryViewModel
import com.devansh.noteapp.ui.screens.home.HomeScreenViewModel
import com.devansh.noteapp.ui.screens.setting.SettingViewModel
import com.devansh.noteapp.ui.screens.splash.SplashScreenViewModel
import com.devansh.noteapp.ui.theme.DefaultThemeState
import com.devansh.noteapp.ui.theme.NoteThemes
import com.devansh.noteapp.ui.theme.ThemeConfig
import com.devansh.noteapp.ui.theme.ThemeState
import com.devansh.noteapp.ui.theme.ThemeStatelessViewModel
import com.devansh.noteapp.ui.theme.ThemeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val screenModelsModule = module {
    viewModel { ThemeStatelessViewModel(get()) }
    viewModel { ThemeViewModel() }
    viewModel { BaseScreenViewModel() }
    viewModel { SplashScreenViewModel(get(),get()) }
    viewModel { AuthViewModel(get(),get()) }
    viewModel { HomeScreenViewModel(get(),get(),get()) }
    viewModel { CategoryViewModel(get(),get(),get()) }
    viewModel { AddEditNoteViewModel(get(), get(), get()) }
    viewModel { SettingViewModel(get(), get()) }
}

val repositoryModule = module {
    single<NoteDataSource> { NoteDataSourceImpl(get(), get()) }
    single<CategoryDataSource> { CategoryDataSourceImpl(get(), get()) }
    single<CategoryService> { CategoryServiceImpl(get()) }
    single<NoteService> { NoteServiceImpl(get()) }
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

val theme = module {
    single<ThemeState> {
        DefaultThemeState(
            defaultConfig = ThemeConfig(
                defaultTheme = NoteThemes.Light,
                lightTheme = NoteThemes.Light,
                darkTheme = NoteThemes.Dark,
            )
        )
    }
}

val appModules = listOf(
    platformModule(),
    theme,
    dataModule,
    repositoryModule,
    screenModelsModule,
    dispatcherModule,
)