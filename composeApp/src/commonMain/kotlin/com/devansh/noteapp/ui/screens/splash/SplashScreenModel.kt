package com.devansh.noteapp.ui.screens.splash

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
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
) : ViewModel() {

    fun isSyncAutoEnable(): Boolean = pref.autoSyncDB

    fun isUserLoggedIn(): Boolean = pref.isLoggedIn

    suspend fun syncDatabase(): Unit = withContext(Dispatchers.IO) {
        try {
            val unsyncedNotes = noteDataSource.getUnSyncedNotes()

            noteRemoteDao.upsert(unsyncedNotes, pref.accessToken)

        } catch (e: Exception) {
            Logger.e("SyncError", e) { "Unexpected error occurred during sync" }
        }
    }
}