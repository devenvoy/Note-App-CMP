package com.devansh.noteapp.di

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun getHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient{
        install(Logging) { level = LogLevel.ALL }
        install(ContentNegotiation) {
            json(json = Json { ignoreUnknownKeys = true })
        }
        config()
    }
}