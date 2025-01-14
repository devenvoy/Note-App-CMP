package com.devansh.noteapp.domain.repo

interface AppCacheSetting {

    var accessToken: String

    val isLoggedIn: Boolean

    var autoSyncDB : Boolean

    fun logout(callBack:()->Unit)
}