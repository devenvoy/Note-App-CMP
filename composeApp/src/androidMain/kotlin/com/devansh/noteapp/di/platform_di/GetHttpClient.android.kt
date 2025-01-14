package com.devansh.noteapp.di.platform_di

import android.content.Context
import android.content.pm.ApplicationInfo
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.devansh.noteapp.NoteApp
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun getHttpClient(): HttpClient {
    return HttpClient(OkHttp) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        if (isDebugBuild(NoteApp.AppContext)) {
            engine {
                addInterceptor(getChuckerInterceptor(NoteApp.AppContext))
            }
        }
    }
}

private fun isDebugBuild(context: Context): Boolean {
    return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
}

private fun getChuckerInterceptor(context: Context): ChuckerInterceptor {
    return ChuckerInterceptor.Builder(context = context)
        .collector(collector = ChuckerCollector(context, showNotification = true))
        .maxContentLength(Long.MAX_VALUE)
        .alwaysReadResponseBody(true)
        .build()
}
