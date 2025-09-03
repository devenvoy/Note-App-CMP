package com.devansh.noteapp.di.platform_di

interface FirebaseRemoteConfigReader {
    fun isForceUpdateEnabled(): Boolean
}

expect suspend fun getFirebaseRemoteConfigReader(): FirebaseRemoteConfigReader