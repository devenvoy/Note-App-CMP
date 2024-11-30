package com.devansh.noteapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform