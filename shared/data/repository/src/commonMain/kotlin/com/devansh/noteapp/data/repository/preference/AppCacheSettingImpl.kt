package com.devansh.noteapp.data.repository.preference

import com.devansh.noteapp.core.utils.UnitCBF
import com.devansh.noteapp.data.models.dto.settings.AppColor
import com.devansh.noteapp.data.models.dto.settings.AppColor.Companion.toInt
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentDisplayMode
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentDisplayMode.Companion.toInt
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentOverflowStyle
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentOverflowStyle.Companion.toInt
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentSize
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentSize.Companion.toInt
import com.devansh.noteapp.data.repository.AppCacheSetting
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getFloatFlow
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getStringFlow
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
class AppCacheSettingImpl : AppCacheSetting {

    private val settings: Settings by lazy { Settings() }
    private val observableSettings: ObservableSettings by lazy { settings as ObservableSettings }

    override var accessToken: String?
        get() = settings[SettingStorageKeys.ACCESS_TOKEN.key]
        set(value) {
            settings[SettingStorageKeys.ACCESS_TOKEN.key] = value
        }

    override var refreshToken: String?
        get() = settings[SettingStorageKeys.REFRESH_TOKEN.key]
        set(value) {
            settings[SettingStorageKeys.REFRESH_TOKEN.key] = value
        }

    override val isLoggedIn: Boolean
        get() = (settings[SettingStorageKeys.ACCESS_TOKEN.key] ?: "").isNotEmpty()

    override val isOnBoardComplete: Boolean
        get() = (settings[SettingStorageKeys.ONBOARD_COMPLETE.key] ?: false)

    override val autoSyncDB: Flow<Boolean>
        get() = observableSettings.getBooleanFlow(
            SettingStorageKeys.AUTO_SYNC_WITH_REMOTE.key,
            true
        )

    override val userEmail: String
        get() = settings[SettingStorageKeys.USER_EMAIL.key, ""]

    @OptIn(ExperimentalCoroutinesApi::class)
    override val theme: Flow<Int> =
        observableSettings.getIntFlow(SettingStorageKeys.THEME.key, -1)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val color: Flow<AppColor> =
        observableSettings.getIntFlow(SettingStorageKeys.COLOR.key, AppColor.DYNAMIC.toInt())
            .mapLatest { AppColor.fromInt(it) }

    override val isListView: Flow<Boolean>
        get() = observableSettings.getBooleanFlow(SettingStorageKeys.IS_LIST_VIEW.key, false)

    override val dateFormatter: Flow<String>
        get() = observableSettings.getStringFlow(SettingStorageKeys.DATE_FORMATTER.key, "")

    override val timeFormatter: Flow<String>
        get() = observableSettings.getStringFlow(SettingStorageKeys.TIME_FORMATTER.key, "")

    override val isScreenProtected: Flow<Boolean>
        get() = observableSettings.getBooleanFlow(SettingStorageKeys.IS_SCREEN_PROTECTED.key, false)

    override val fontScale: Flow<Float>
        get() = observableSettings.getFloatFlow(SettingStorageKeys.FONT_SCALE.key, 1f)

    override val backupFrequency: Flow<Int>
        get() = observableSettings.getIntFlow(SettingStorageKeys.BACKUP_FREQUENCY.key, 0)

    override val password: Flow<String>
        get() = observableSettings.getStringFlow(SettingStorageKeys.PASSWORD.key, "")

    override val biometricAuthEnabled: Flow<Boolean>
        get() = observableSettings.getBooleanFlow(SettingStorageKeys.BIOMETRIC_AUTH_ENABLED.key, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val enumOverflowStyle: Flow<ListNoteContentOverflowStyle> =
        observableSettings.getIntFlow(SettingStorageKeys.ENUM_OVERFLOW_STYLE.key, ListNoteContentOverflowStyle.ELLIPSIS.toInt())
            .mapLatest { ListNoteContentOverflowStyle.fromInt(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val enumContentSize: Flow<ListNoteContentSize> =
        observableSettings.getIntFlow(SettingStorageKeys.ENUM_CONTENT_SIZE.key, ListNoteContentSize.DEFAULT.toInt())
            .mapLatest { ListNoteContentSize.fromInt(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val enumDisplayMode: Flow<ListNoteContentDisplayMode> =
        observableSettings.getIntFlow(SettingStorageKeys.ENUM_DISPLAY_MODE.key, ListNoteContentDisplayMode.RAW.toInt())
            .mapLatest { ListNoteContentDisplayMode.fromInt(it) }

    override val isAutoSaveEnabled: Flow<Boolean>
        get() = observableSettings.getBooleanFlow(SettingStorageKeys.IS_AUTO_SAVE_ENABLED.key, false)

    override val titleAlignment: Flow<Int>
        get() = observableSettings.getIntFlow(SettingStorageKeys.TITLE_ALIGNMENT.key, 0)

    override val showLineNumbers: Flow<Boolean>
        get() = observableSettings.getBooleanFlow(SettingStorageKeys.SHOW_LINE_NUMBERS.key, false)

    override fun logout(callBack: UnitCBF) {
        settings.clear()
        callBack()
    }

    override fun setUserEmail(email: String) {
        settings[SettingStorageKeys.USER_EMAIL.key] = email
    }

    // Settings State setters
    override suspend fun setTheme(theme: Int) {
        putPreferenceValue(SettingStorageKeys.THEME.key, theme)
    }

    override suspend fun setColor(color: AppColor) {
        putPreferenceValue(SettingStorageKeys.COLOR.key, color.toInt())
    }

    override suspend fun setIsListView(isListView: Boolean) {
        putPreferenceValue(SettingStorageKeys.IS_LIST_VIEW.key, isListView)
    }

    override suspend fun setDateFormatter(formatter: String) {
        putPreferenceValue(SettingStorageKeys.DATE_FORMATTER.key, formatter)
    }

    override suspend fun setTimeFormatter(formatter: String) {
        putPreferenceValue(SettingStorageKeys.TIME_FORMATTER.key, formatter)
    }

    override suspend fun setIsScreenProtected(isProtected: Boolean) {
        putPreferenceValue(SettingStorageKeys.IS_SCREEN_PROTECTED.key, isProtected)
    }

    override suspend fun setFontScale(scale: Float) {
        putPreferenceValue(SettingStorageKeys.FONT_SCALE.key, scale)
    }

    override suspend fun setBackupFrequency(frequency: Int) {
        putPreferenceValue(SettingStorageKeys.BACKUP_FREQUENCY.key, frequency)
    }

    override suspend fun setPassword(password: String) {
        putPreferenceValue(SettingStorageKeys.PASSWORD.key, password)
    }

    override suspend fun setBiometricAuthEnabled(enabled: Boolean) {
        putPreferenceValue(SettingStorageKeys.BIOMETRIC_AUTH_ENABLED.key, enabled)
    }

    override suspend fun setEnumOverflowStyle(style: ListNoteContentOverflowStyle) {
        putPreferenceValue(SettingStorageKeys.ENUM_OVERFLOW_STYLE.key, style.toInt())
    }

    override suspend fun setEnumContentSize(size: ListNoteContentSize) {
        putPreferenceValue(SettingStorageKeys.ENUM_CONTENT_SIZE.key, size.toInt())
    }

    override suspend fun setEnumDisplayMode(mode: ListNoteContentDisplayMode) {
        putPreferenceValue(SettingStorageKeys.ENUM_DISPLAY_MODE.key, mode.toInt())
    }

    override suspend fun setIsAutoSaveEnabled(enabled: Boolean) {
        putPreferenceValue(SettingStorageKeys.IS_AUTO_SAVE_ENABLED.key, enabled)
    }

    override suspend fun setTitleAlignment(alignment: Int) {
        putPreferenceValue(SettingStorageKeys.TITLE_ALIGNMENT.key, alignment)
    }

    override suspend fun setShowLineNumbers(showNumbers: Boolean) {
        putPreferenceValue(SettingStorageKeys.SHOW_LINE_NUMBERS.key, showNumbers)
    }

    override suspend fun setOnBoardStatus(flag: Boolean) {
        putPreferenceValue(SettingStorageKeys.ONBOARD_COMPLETE.key, flag)
    }

    override suspend fun <T> putPreferenceValue(key: String, value: T) {
        withContext(Dispatchers.IO) {
            when (value) {
                is Int -> observableSettings.putInt(key, value)
                is Float -> observableSettings.putFloat(key, value)
                is Boolean -> observableSettings.putBoolean(key, value)
                is String -> observableSettings.putString(key, value)
                else -> throw IllegalArgumentException("Unsupported value type")
            }
        }
    }
}