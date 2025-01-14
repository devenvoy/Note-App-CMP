package com.devansh.noteapp.data.preference

import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class AppCacheSettingImpl : AppCacheSetting {

    private val settings: Settings by lazy { Settings() }
    private val observableSettings: ObservableSettings by lazy { settings as ObservableSettings }

    override var accessToken: String
        get() = settings[SettingStorageKeys.ACCESS_TOKEN.key] ?: ""
        set(value) {
            settings[SettingStorageKeys.ACCESS_TOKEN.key] = value
        }

    override val isLoggedIn: Boolean
        get() = (settings[SettingStorageKeys.ACCESS_TOKEN.key] ?: "").isNotEmpty()

    override var autoSyncDB: Boolean
        get() = settings[SettingStorageKeys.AUTO_SYNC_WITH_REMOTE.key] ?: true
        set(value) {
            settings[SettingStorageKeys.AUTO_SYNC_WITH_REMOTE.key] = value
        }

    override fun logout(callBack: () -> Unit) {
        settings.clear()
        callBack()
    }
}