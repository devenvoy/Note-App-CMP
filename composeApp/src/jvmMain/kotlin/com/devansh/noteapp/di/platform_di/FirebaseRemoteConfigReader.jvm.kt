package com.devansh.noteapp.di.platform_di


actual suspend fun getFirebaseRemoteConfigReader(): FirebaseRemoteConfigReader {
    return DesktopFirebaseRemoteConfigReader()
}

class DesktopFirebaseRemoteConfigReader() : FirebaseRemoteConfigReader {
    override fun isForceUpdateEnabled(): Boolean {
        return false
    }
}