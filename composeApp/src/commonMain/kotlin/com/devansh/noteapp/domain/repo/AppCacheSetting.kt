package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.ui.screens.core.ListType
import kotlinx.coroutines.flow.Flow

interface AppCacheSetting {

    var accessToken: String?

    var refreshToken: String?

    var autoSyncDB: Boolean

    val isLoggedIn: Boolean

    val observableAutoSyncDB : Flow<Boolean>

    val observableListType : Flow<ListType>
    var listType : Int

    val userEmail : String

    fun logout(callBack: UnitCBF)

    fun setUserEmail(email:String)
}