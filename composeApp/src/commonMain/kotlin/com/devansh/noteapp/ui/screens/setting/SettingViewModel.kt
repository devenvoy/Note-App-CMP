package com.devansh.noteapp.ui.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.ui.screens.core.ListType
import kotlinx.coroutines.launch

class SettingViewModel(
    private val pref: AppCacheSetting,
    private val noteRepo: NoteDataSource
) : ViewModel() {


    val userEmail = pref.userEmail
    val autoSyncDB = pref.observableAutoSyncDB

    val listType = pref.observableListType

    fun logOut() {
        pref.logout { viewModelScope.launch { noteRepo.emptyNoteTable() } }
    }

    fun updateAutoSyncDB(autoSyncDB: Boolean) {
        viewModelScope.launch { pref.autoSyncDB = autoSyncDB }
    }

    fun updateListType(listType: ListType) {
        viewModelScope.launch { pref.listType = listType.ordinal }
    }
}