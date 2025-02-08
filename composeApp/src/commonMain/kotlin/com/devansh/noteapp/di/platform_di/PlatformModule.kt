package com.devansh.noteapp.di.platform_di

import org.koin.core.module.Module

expect fun platformModule() : Module

expect fun shareText(text: String, mimeType: String)

expect fun isDesktop():Boolean
