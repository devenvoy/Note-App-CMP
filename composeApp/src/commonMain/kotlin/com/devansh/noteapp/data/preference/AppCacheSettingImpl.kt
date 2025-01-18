package com.devansh.noteapp.data.preference

import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.ui.screens.core.ListType
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getStringFlow
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalSettingsApi::class)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override val observableListType: Flow<ListType>
        get() = observableSettings.getIntFlow(SettingStorageKeys.LIST_TYPE_KEY.key,0)
            .mapLatest { i -> ListType.entries.first { it.ordinal == i } }

    override var listType: Int
        get() = settings[SettingStorageKeys.LIST_TYPE_KEY.key,0]
        set(value) {
            settings[SettingStorageKeys.LIST_TYPE_KEY.key] = value
        }

    override val observableAutoSyncDB: Flow<Boolean>
        get() = observableSettings.getBooleanFlow(SettingStorageKeys.AUTO_SYNC_WITH_REMOTE.key, true)

    override fun logout(callBack: () -> Unit) {
        settings.clear()
        callBack()
    }
}