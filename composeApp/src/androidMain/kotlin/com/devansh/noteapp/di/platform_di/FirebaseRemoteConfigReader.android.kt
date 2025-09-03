package com.devansh.noteapp.di.platform_di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await

actual suspend fun getFirebaseRemoteConfigReader(): FirebaseRemoteConfigReader {

    val remoteConfigMap = mutableMapOf<String, Boolean>()

    val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    firebaseRemoteConfig.setConfigSettingsAsync(
        FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
    )

    firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            println("---> Remote config activated")

            firebaseRemoteConfig.all.forEach { key, value ->
                remoteConfigMap.put(key, value.asBoolean())
            }
        } else {
            println("---> Failed to activate remote config")
        }
    }.await()

    return object : FirebaseRemoteConfigReader {
        override fun isForceUpdateEnabled(): Boolean {
            println("---> remoteConfigDataList: $remoteConfigMap")
            return remoteConfigMap["test_config"] ?: false
        }
    }
}

class AndroidFirebaseRemoteConfigReader() : FirebaseRemoteConfigReader {
    override fun isForceUpdateEnabled(): Boolean {
        return false
    }
}