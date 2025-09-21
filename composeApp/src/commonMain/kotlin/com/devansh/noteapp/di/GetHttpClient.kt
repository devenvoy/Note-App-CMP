package com.devansh.noteapp.di

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

expect fun getHttpClient(
    config: HttpClientConfig<*>.() -> Unit
) :HttpClient