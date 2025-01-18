package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.ui.screens.core.ListType
import kotlinx.coroutines.flow.Flow

interface AppCacheSetting {

    var accessToken: String

    val isLoggedIn: Boolean

    val observableAutoSyncDB : Flow<Boolean>
    var autoSyncDB : Boolean

    val observableListType : Flow<ListType>
    var listType : Int

    val userEmail : String

    fun logout(callBack:()->Unit)

    fun setUserEmail(email:String)
}