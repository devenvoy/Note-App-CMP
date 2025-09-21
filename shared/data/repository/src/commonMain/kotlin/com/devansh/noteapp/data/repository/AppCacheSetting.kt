package com.devansh.noteapp.data.repository

import com.devansh.noteapp.core.utils.UnitCBF
import com.devansh.noteapp.data.models.dto.settings.AppColor
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentDisplayMode
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentOverflowStyle
import com.devansh.noteapp.data.models.dto.settings.ListNoteContentSize
import com.devansh.noteapp.data.models.dto.settings.ListType
import kotlinx.coroutines.flow.Flow

interface AppCacheSetting {

    var accessToken: String?
    var refreshToken: String?
    val isLoggedIn: Boolean
    val userEmail : String
    val isOnBoardComplete: Boolean

    val listType: Flow<ListType>
    val autoSyncDB: Flow<Boolean>
    val theme: Flow<Int>
    val color: Flow<AppColor>
    val isAppInDarkMode: Flow<Boolean>
    val shouldFollowSystem: Flow<Boolean>
    val isSwitchActive: Flow<Boolean>
    val isListView: Flow<Boolean>
    val dateFormatter: Flow<String>
    val timeFormatter: Flow<String>
    val isScreenProtected: Flow<Boolean>
    val fontScale: Flow<Float>
    val backupFrequency: Flow<Int>
    val password: Flow<String>
    val biometricAuthEnabled: Flow<Boolean>
    val enumOverflowStyle: Flow<ListNoteContentOverflowStyle>
    val enumContentSize: Flow<ListNoteContentSize>
    val enumDisplayMode: Flow<ListNoteContentDisplayMode>
    val isAutoSaveEnabled: Flow<Boolean>
    val titleAlignment: Flow<Int>
    val showLineNumbers: Flow<Boolean>


    fun logout(callBack: UnitCBF)

    fun setUserEmail(email:String)

    // Settings State setters
    suspend fun setTheme(theme: Int)
    suspend fun setColor(color: AppColor)
    suspend fun setIsAppInDarkMode(isDarkMode: Boolean)
    suspend fun setShouldFollowSystem(shouldFollow: Boolean)
    suspend fun setIsSwitchActive(isActive: Boolean)
    suspend fun setIsListView(isListView: Boolean)
    suspend fun setDateFormatter(formatter: String)
    suspend fun setTimeFormatter(formatter: String)
    suspend fun setIsScreenProtected(isProtected: Boolean)
    suspend fun setFontScale(scale: Float)
    suspend fun setBackupFrequency(frequency: Int)
    suspend fun setPassword(password: String)
    suspend fun setBiometricAuthEnabled(enabled: Boolean)
    suspend fun setEnumOverflowStyle(style: ListNoteContentOverflowStyle)
    suspend fun setEnumContentSize(size: ListNoteContentSize)
    suspend fun setEnumDisplayMode(mode: ListNoteContentDisplayMode)
    suspend fun setIsAutoSaveEnabled(enabled: Boolean)
    suspend fun setTitleAlignment(alignment: Int)
    suspend fun setShowLineNumbers(showNumbers: Boolean)
    suspend fun setOnBoardStatus(flag: Boolean)

    suspend fun <T> putPreferenceValue(key: String, value: T)
}