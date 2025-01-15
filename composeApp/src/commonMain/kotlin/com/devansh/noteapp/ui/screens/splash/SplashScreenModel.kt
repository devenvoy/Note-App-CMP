package com.devansh.noteapp.ui.screens.splash

import cafe.adriel.voyager.core.model.ScreenModel
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Logger.Companion.e
import com.devansh.noteapp.data.remote.utils.onError
import com.devansh.noteapp.data.remote.utils.onSuccess
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.domain.repo.NoteRemoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SplashScreenModel(
    private val pref: AppCacheSetting,
    private val noteDataSource: NoteDataSource,
    private val noteRemoteDao: NoteRemoteDao
) : ScreenModel {

    fun isSyncAutoEnable(): Boolean = pref.autoSyncDB

    fun isUserLoggedIn(): Boolean = pref.isLoggedIn

    suspend fun syncDatabase(): Unit = withContext(Dispatchers.IO) {
        try {
            val unsyncedNotes = noteDataSource.getUnSyncedNotes()

            noteRemoteDao.upsert(unsyncedNotes, pref.accessToken)

            val result = noteRemoteDao.getNotes(pref.accessToken)

            result.onSuccess { response ->
                if (response.status) {
                    response.value?.notes?.forEach { note ->
                        noteDataSource.insertNote(note, true)
                    }
                } else {
                    Logger.e("SyncError", null) { "${response.detail}" }
                    Logger.e("SyncError", null) { "Failed to sync data" }
                }
            }.onError {
                Logger.e("SyncError", null) { "Failed to sync data" }
            }
        } catch (e: Exception) {
            Logger.e("SyncError", e) { "Unexpected error occurred during sync" }
        }
    }
}