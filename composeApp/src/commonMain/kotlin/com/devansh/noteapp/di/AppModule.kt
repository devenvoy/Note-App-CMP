package com.devansh.noteapp.di

import com.devansh.noteapp.BuildConfig
import com.devansh.noteapp.core.database.datasource.CategoryDataSourceImpl
import com.devansh.noteapp.core.database.datasource.NoteDataSourceImpl
import com.devansh.noteapp.core.database.repo.CategoryDataSource
import com.devansh.noteapp.core.database.repo.NoteDataSource
import com.devansh.noteapp.core.designsystem.theme.DefaultThemeState
import com.devansh.noteapp.core.designsystem.theme.NoteThemes
import com.devansh.noteapp.core.designsystem.theme.ThemeConfig
import com.devansh.noteapp.core.designsystem.theme.ThemeState
import com.devansh.noteapp.core.designsystem.theme.ThemeStatelessViewModel
import com.devansh.noteapp.data.models.dto.settings.AppTheme
import com.devansh.noteapp.data.repository.AppCacheSetting
import com.devansh.noteapp.data.repository.impl.AuthServiceImpl
import com.devansh.noteapp.data.repository.impl.CategoryServiceImpl
import com.devansh.noteapp.data.repository.impl.NoteServiceImpl
import com.devansh.noteapp.data.repository.preference.AppCacheSettingImpl
import com.devansh.noteapp.data.repository.repo.AuthService
import com.devansh.noteapp.data.repository.repo.CategoryService
import com.devansh.noteapp.data.repository.repo.NoteService
import com.devansh.noteapp.feature.auth.presentation.splash.SplashScreenViewModel
import com.devansh.noteapp.feature.authentication.AuthViewModel
import com.devansh.noteapp.feature.notes.presentation.add_edit.AddEditNoteViewModel
import com.devansh.noteapp.feature.notes.presentation.category.CategoryViewModel
import com.devansh.noteapp.feature.notes.presentation.notes.HomeScreenViewModel
import com.devansh.noteapp.feature.settings.presentation.BaseScreenViewModel
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import kotlinx.coroutines.flow.map
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val screenModelsModule = module {
    viewModel {
        ThemeStatelessViewModel(theme = get<AppCacheSetting>().theme.map(AppTheme::fromInt))
    }
    viewModel { BaseScreenViewModel(get(), get()) }
    viewModel { SplashScreenViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { HomeScreenViewModel(get(), get()) }
    viewModel { CategoryViewModel(get(), get(), get()) }
    viewModel { (id: Long) -> AddEditNoteViewModel(id, get(), get(), get()) }
}

val repositoryModule = module {
    single<NoteDataSource> { NoteDataSourceImpl(get()) }
    single<CategoryDataSource> { CategoryDataSourceImpl(get()) }
    single<CategoryService> { CategoryServiceImpl(get()) }
    single<NoteService> { NoteServiceImpl(get()) }
    single<AuthService> { AuthServiceImpl(get()) }
}

val dispatcherModule = module {
}

val theme = module {
    single<AppCacheSetting> { AppCacheSettingImpl(get()) }
    single {
        getHttpClient {
            install(DefaultRequest){
                url{ takeFrom(BuildConfig.BASE_URL)}
                contentType(ContentType.Application.Json)
                get<AppCacheSetting>().accessToken?.let {
                    header(HttpHeaders.Authorization,"Bearer $it")
                }
            }
        }
    }
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
    repositoryModule,
    screenModelsModule,
    dispatcherModule,
)