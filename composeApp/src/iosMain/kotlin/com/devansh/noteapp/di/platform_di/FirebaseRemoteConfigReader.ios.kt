package com.devansh.noteapp.di.platform_di

actual suspend fun getFirebaseRemoteConfigReader(): FirebaseRemoteConfigReader {
   return IosFirebaseRemoteConfigReader()
}

class IosFirebaseRemoteConfigReader() : FirebaseRemoteConfigReader {
    override fun isForceUpdateEnabled(): Boolean {
        return IosFirebaseRemoteConfig.getIsForceUpdateEnabled()
    }
}

object IosFirebaseRemoteConfig {
    private var isForceUpdateEnabled: Boolean = false

    fun setIsForceUpdateEnabled(value: Boolean) {
        println("---> setIsForceUpdateEnabled: $value")
        isForceUpdateEnabled = value
    }

    fun getIsForceUpdateEnabled(): Boolean {
        println("---> getIsForceUpdateEnabled: $isForceUpdateEnabled")
        return isForceUpdateEnabled
    }
}