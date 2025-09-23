package com.devansh.noteapp.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.core.database.repo.NoteDataSource
import com.devansh.noteapp.core.designsystem.SettingsState
import com.devansh.noteapp.data.models.dto.settings.AppColor
import com.devansh.noteapp.data.models.dto.settings.AppTheme
import com.devansh.noteapp.data.models.dto.settings.AppTheme.Companion.toInt
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentDisplayMode
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentOverflowStyle
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentSize
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

class BaseScreenViewModel(
    private val pref: AppCacheSetting,
    private val noteRepo: NoteDataSource
) : ViewModel() {

    val userEmail = pref.userEmail

    val autoSyncDB =
        pref.autoSyncDB.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(3000), true)

    val settingsStateFlow: StateFlow<SettingsState> = combine<Comparable<*>, SettingsState>(
        pref.theme,
        pref.color,
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
            theme = AppTheme.Companion.fromInt(values[0] as Int),
            color = values[1] as AppColor,
            isListView = values[2] as Boolean,
            dateFormatter = values[3] as String,
            timeFormatter = values[4] as String,
            isScreenProtected = values[5] as Boolean,
            fontScale = values[6] as Float,
            backupFrequency = values[7] as Int,
            password = values[8] as String,
            biometricAuthEnabled = values[9] as Boolean,
            enumOverflowStyle = values[10] as ListNoteContentOverflowStyle,
            enumContentSize = values[11] as ListNoteContentSize,
            enumDisplayMode = values[12] as ListNoteContentDisplayMode,
            isAutoSaveEnabled = values[13] as Boolean,
            titleAlignment = values[14] as Int,
            showLineNumbers = values[15] as Boolean
        )
    }.flowOn(Dispatchers.IO).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.Eagerly,
        initialValue = SettingsState()
    )

    // Setting update functions
    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch { pref.setTheme(theme.toInt()) }
    }

    fun updateColor(color: AppColor) {
        viewModelScope.launch { pref.setColor(color) }
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
}