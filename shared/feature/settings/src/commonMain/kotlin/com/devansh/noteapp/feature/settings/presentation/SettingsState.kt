package com.devansh.noteapp.feature.settings.presentation

import androidx.compose.runtime.Stable
import com.devansh.noteapp.core.designsystem.AppTheme
import com.devansh.noteapp.data.models.dto.settings.AppColor
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentDisplayMode
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentOverflowStyle
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentSize

@Stable
data class SettingsState(
    val theme: AppTheme = AppTheme.UNDEFINED,
    val color: AppColor = AppColor.DYNAMIC,
    val isAppInDarkMode: Boolean = false,
    val shouldFollowSystem: Boolean = false,
    val isSwitchActive: Boolean = false,
    val isListView: Boolean = false,
    val dateFormatter: String = "",
    val timeFormatter: String = "",
    val isScreenProtected: Boolean = false,
    val fontScale: Float = 1f,
    val backupFrequency: Int = 0,
    val password: String = "",
    val biometricAuthEnabled: Boolean = false,
    val enumOverflowStyle: ListNoteContentOverflowStyle = ListNoteContentOverflowStyle.ELLIPSIS,
    val enumContentSize: ListNoteContentSize = ListNoteContentSize.DEFAULT,
    val enumDisplayMode: ListNoteContentDisplayMode = ListNoteContentDisplayMode.RAW,
    val isAutoSaveEnabled: Boolean = false,
    val titleAlignment: Int = 0,
    val showLineNumbers: Boolean = false
)