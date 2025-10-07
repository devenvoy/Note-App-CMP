package com.devansh.noteapp.data.repository

import com.devansh.noteapp.core.utils.UnitCBF
import com.devansh.noteapp.data.models.dto.settings.AppColor
import com.devansh.noteapp.data.models.dto.settings.AppColor.Companion.toInt
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentDisplayMode
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentDisplayMode.Companion.toInt
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentOverflowStyle
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentOverflowStyle.Companion.toInt
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentSize
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentSize.Companion.toInt
import com.devansh.noteapp.data.repository.preference.SettingStorageKeys
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class WebAppCacheSettingImpl(
    private val settings: Settings = Settings(),
) : AppCacheSetting {

    override var accessToken: String?
        get() = settings.getStringOrNull(SettingStorageKeys.ACCESS_TOKEN.key)
        set(value) {
            if (value != null)
                settings.putString(SettingStorageKeys.ACCESS_TOKEN.key, value)
            else
                settings.remove(SettingStorageKeys.ACCESS_TOKEN.key)
        }

    override var refreshToken: String?
        get() = settings.getStringOrNull(SettingStorageKeys.REFRESH_TOKEN.key)
        set(value) {
            if (value != null)
                settings.putString(SettingStorageKeys.REFRESH_TOKEN.key, value)
            else
                settings.remove(SettingStorageKeys.REFRESH_TOKEN.key)
        }

    override val isLoggedIn: Boolean
        get() = !accessToken.isNullOrEmpty()

    override val isOnBoardComplete: Boolean
        get() = settings.getBoolean(SettingStorageKeys.ONBOARD_COMPLETE.key, false)

    override val autoSyncDB: Flow<Boolean> =
        MutableStateFlow(settings.getBoolean(SettingStorageKeys.AUTO_SYNC_WITH_REMOTE.key, true))

    override val userEmail: String
        get() = settings.getString(SettingStorageKeys.USER_EMAIL.key, "")

    override val theme: Flow<Int> =
        MutableStateFlow(settings.getInt(SettingStorageKeys.THEME.key, -1))

    override val color: Flow<AppColor> =
        MutableStateFlow(
            AppColor.fromInt(
                settings.getInt(
                    SettingStorageKeys.COLOR.key,
                    AppColor.DYNAMIC.toInt()
                )
            )
        )

    override val isListView: Flow<Boolean> =
        MutableStateFlow(settings.getBoolean(SettingStorageKeys.IS_LIST_VIEW.key, false))

    override val dateFormatter: Flow<String> =
        MutableStateFlow(settings.getString(SettingStorageKeys.DATE_FORMATTER.key, ""))

    override val timeFormatter: Flow<String> =
        MutableStateFlow(settings.getString(SettingStorageKeys.TIME_FORMATTER.key, ""))

    override val isScreenProtected: Flow<Boolean> =
        MutableStateFlow(settings.getBoolean(SettingStorageKeys.IS_SCREEN_PROTECTED.key, false))

    override val fontScale: Flow<Float> =
        MutableStateFlow(settings.getFloat(SettingStorageKeys.FONT_SCALE.key, 1f))

    override val backupFrequency: Flow<Int> =
        MutableStateFlow(settings.getInt(SettingStorageKeys.BACKUP_FREQUENCY.key, 0))

    override val password: Flow<String> =
        MutableStateFlow(settings.getString(SettingStorageKeys.PASSWORD.key, ""))

    override val biometricAuthEnabled: Flow<Boolean> =
        MutableStateFlow(settings.getBoolean(SettingStorageKeys.BIOMETRIC_AUTH_ENABLED.key, false))

    override val enumOverflowStyle: Flow<ListNoteContentOverflowStyle> =
        MutableStateFlow(
            ListNoteContentOverflowStyle.fromInt(
                settings.getInt(
                    SettingStorageKeys.ENUM_OVERFLOW_STYLE.key,
                    ListNoteContentOverflowStyle.ELLIPSIS.toInt()
                )
            )
        )

    override val enumContentSize: Flow<ListNoteContentSize> =
        MutableStateFlow(
            ListNoteContentSize.fromInt(
                settings.getInt(
                    SettingStorageKeys.ENUM_CONTENT_SIZE.key,
                    ListNoteContentSize.DEFAULT.toInt()
                )
            )
        )

    override val enumDisplayMode: Flow<ListNoteContentDisplayMode> =
        MutableStateFlow(
            ListNoteContentDisplayMode.fromInt(
                settings.getInt(
                    SettingStorageKeys.ENUM_DISPLAY_MODE.key,
                    ListNoteContentDisplayMode.RAW.toInt()
                )
            )
        )

    override val isAutoSaveEnabled: Flow<Boolean> =
        MutableStateFlow(settings.getBoolean(SettingStorageKeys.IS_AUTO_SAVE_ENABLED.key, false))

    override val titleAlignment: Flow<Int> =
        MutableStateFlow(settings.getInt(SettingStorageKeys.TITLE_ALIGNMENT.key, 0))

    override val showLineNumbers: Flow<Boolean> =
        MutableStateFlow(settings.getBoolean(SettingStorageKeys.SHOW_LINE_NUMBERS.key, false))

    override fun logout(callBack: UnitCBF) {
        settings.clear()
        callBack()
    }

    override fun setUserEmail(email: String) {
        settings.putString(SettingStorageKeys.USER_EMAIL.key, email)
    }

    // Settings State setters
    override suspend fun setTheme(theme: Int) =
        putPreferenceValue(SettingStorageKeys.THEME.key, theme)

    override suspend fun setColor(color: AppColor) =
        putPreferenceValue(SettingStorageKeys.COLOR.key, color.toInt())

    override suspend fun setIsListView(isListView: Boolean) =
        putPreferenceValue(SettingStorageKeys.IS_LIST_VIEW.key, isListView)

    override suspend fun setDateFormatter(formatter: String) =
        putPreferenceValue(SettingStorageKeys.DATE_FORMATTER.key, formatter)

    override suspend fun setTimeFormatter(formatter: String) =
        putPreferenceValue(SettingStorageKeys.TIME_FORMATTER.key, formatter)

    override suspend fun setIsScreenProtected(isProtected: Boolean) =
        putPreferenceValue(SettingStorageKeys.IS_SCREEN_PROTECTED.key, isProtected)

    override suspend fun setFontScale(scale: Float) =
        putPreferenceValue(SettingStorageKeys.FONT_SCALE.key, scale)

    override suspend fun setBackupFrequency(frequency: Int) =
        putPreferenceValue(SettingStorageKeys.BACKUP_FREQUENCY.key, frequency)

    override suspend fun setPassword(password: String) =
        putPreferenceValue(SettingStorageKeys.PASSWORD.key, password)

    override suspend fun setBiometricAuthEnabled(enabled: Boolean) =
        putPreferenceValue(SettingStorageKeys.BIOMETRIC_AUTH_ENABLED.key, enabled)

    override suspend fun setEnumOverflowStyle(style: ListNoteContentOverflowStyle) =
        putPreferenceValue(SettingStorageKeys.ENUM_OVERFLOW_STYLE.key, style.toInt())

    override suspend fun setEnumContentSize(size: ListNoteContentSize) =
        putPreferenceValue(SettingStorageKeys.ENUM_CONTENT_SIZE.key, size.toInt())

    override suspend fun setEnumDisplayMode(mode: ListNoteContentDisplayMode) =
        putPreferenceValue(SettingStorageKeys.ENUM_DISPLAY_MODE.key, mode.toInt())

    override suspend fun setIsAutoSaveEnabled(enabled: Boolean) =
        putPreferenceValue(SettingStorageKeys.IS_AUTO_SAVE_ENABLED.key, enabled)

    override suspend fun setTitleAlignment(alignment: Int) =
        putPreferenceValue(SettingStorageKeys.TITLE_ALIGNMENT.key, alignment)

    override suspend fun setShowLineNumbers(showNumbers: Boolean) =
        putPreferenceValue(SettingStorageKeys.SHOW_LINE_NUMBERS.key, showNumbers)

    override suspend fun setOnBoardStatus(flag: Boolean) =
        putPreferenceValue(SettingStorageKeys.ONBOARD_COMPLETE.key, flag)

    override suspend fun <T> putPreferenceValue(key: String, value: T) {
        when (value) {
            is Int -> settings.putInt(key, value)
            is Float -> settings.putFloat(key, value)
            is Boolean -> settings.putBoolean(key, value)
            is String -> settings.putString(key, value)
            else -> throw IllegalArgumentException("Unsupported value type")
        }
    }
}
