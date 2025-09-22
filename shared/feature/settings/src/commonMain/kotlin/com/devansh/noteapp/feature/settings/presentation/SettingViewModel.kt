package com.devansh.noteapp.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.core.database.repo.NoteDataSource
import com.devansh.noteapp.core.designsystem.AppTheme
import com.devansh.noteapp.core.designsystem.AppTheme.Companion.toInt
import com.devansh.noteapp.data.models.dto.settings.AppColor
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentDisplayMode
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentOverflowStyle
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentSize
import com.devansh.noteapp.data.models.dto.settings.ListType
import com.devansh.noteapp.data.repository.AppCacheSetting
import com.devansh.noteapp.data.repository.preference.SettingStorageKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val pref: AppCacheSetting,
    private val noteRepo: NoteDataSource
) : ViewModel() {

    val userEmail = pref.userEmail

    val autoSyncDB =
        pref.autoSyncDB.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), true)

    val listType = pref.listType
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), ListType.LIST)

    val settingsStateFlow: StateFlow<SettingsState> = combine<Comparable<*>, SettingsState>(
        pref.theme,
        pref.color,
        pref.isAppInDarkMode,
        pref.shouldFollowSystem,
        pref.isSwitchActive,
        pref.isListView,
        pref.dateFormatter,
        pref.timeFormatter,
        pref.isScreenProtected,
        pref.fontScale,
        pref.backupFrequency,
        pref.password,
        pref.biometricAuthEnabled,
        pref.enumOverflowStyle,
        pref.enumContentSize,
        pref.enumDisplayMode,
        pref.isAutoSaveEnabled,
        pref.titleAlignment,
        pref.showLineNumbers
    ) { values ->
        SettingsState(
            theme = AppTheme.fromInt(values[0] as Int),
            color = values[1] as AppColor,
            isAppInDarkMode = values[2] as Boolean,
            shouldFollowSystem = values[3] as Boolean,
            isSwitchActive = values[4] as Boolean,
            isListView = values[5] as Boolean,
            dateFormatter = values[6] as String,
            timeFormatter = values[7] as String,
            isScreenProtected = values[8] as Boolean,
            fontScale = values[9] as Float,
            backupFrequency = values[10] as Int,
            password = values[11] as String,
            biometricAuthEnabled = values[12] as Boolean,
            enumOverflowStyle = values[13] as ListNoteContentOverflowStyle,
            enumContentSize = values[14] as ListNoteContentSize,
            enumDisplayMode = values[15] as ListNoteContentDisplayMode,
            isAutoSaveEnabled = values[16] as Boolean,
            titleAlignment = values[17] as Int,
            showLineNumbers = values[18] as Boolean
        )
    }.flowOn(Dispatchers.IO).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SettingsState()
    )

    // Setting update functions
    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch { pref.setTheme(theme.toInt()) }
    }

    fun updateColor(color: AppColor) {
        viewModelScope.launch { pref.setColor(color) }
    }

    fun updateDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch { pref.setIsAppInDarkMode(isDarkMode) }
    }

    fun updateShouldFollowSystem(shouldFollow: Boolean) {
        viewModelScope.launch { pref.setShouldFollowSystem(shouldFollow) }
    }

    fun updateSwitchActive(isActive: Boolean) {
        viewModelScope.launch { pref.setIsSwitchActive(isActive) }
    }

    fun updateListView(isListView: Boolean) {
        viewModelScope.launch { pref.setIsListView(isListView) }
    }

    fun updateDateFormatter(formatter: String) {
        viewModelScope.launch { pref.setDateFormatter(formatter) }
    }

    fun updateTimeFormatter(formatter: String) {
        viewModelScope.launch { pref.setTimeFormatter(formatter) }
    }

    fun updateScreenProtection(isProtected: Boolean) {
        viewModelScope.launch { pref.setIsScreenProtected(isProtected) }
    }

    fun updateFontScale(scale: Float) {
        viewModelScope.launch { pref.setFontScale(scale) }
    }

    fun updateBackupFrequency(frequency: Int) {
        viewModelScope.launch { pref.setBackupFrequency(frequency) }
    }

    fun updatePassword(password: String) {
        viewModelScope.launch { pref.setPassword(password) }
    }

    fun updateBiometricAuth(enabled: Boolean) {
        viewModelScope.launch { pref.setBiometricAuthEnabled(enabled) }
    }

    fun updateOverflowStyle(style: ListNoteContentOverflowStyle) {
        viewModelScope.launch { pref.setEnumOverflowStyle(style) }
    }

    fun updateContentSize(size: ListNoteContentSize) {
        viewModelScope.launch { pref.setEnumContentSize(size) }
    }

    fun updateDisplayMode(mode: ListNoteContentDisplayMode) {
        viewModelScope.launch { pref.setEnumDisplayMode(mode) }
    }

    fun updateAutoSave(enabled: Boolean) {
        viewModelScope.launch { pref.setIsAutoSaveEnabled(enabled) }
    }

    fun updateTitleAlignment(alignment: Int) {
        viewModelScope.launch { pref.setTitleAlignment(alignment) }
    }

    fun updateShowLineNumbers(showNumbers: Boolean) {
        viewModelScope.launch { pref.setShowLineNumbers(showNumbers) }
    }

    fun logOut() {
        pref.logout { viewModelScope.launch { noteRepo.emptyNoteTable() } }
    }

    fun updateAutoSyncDB(autoSyncDB: Boolean) {
        viewModelScope.launch {
            pref.putPreferenceValue(SettingStorageKeys.AUTO_SYNC_WITH_REMOTE.key, autoSyncDB)
        }
    }

    fun updateListType(listType: ListType) {
        viewModelScope.launch {
            pref.putPreferenceValue(SettingStorageKeys.LIST_TYPE_KEY.key, listType)
        }
    }
}