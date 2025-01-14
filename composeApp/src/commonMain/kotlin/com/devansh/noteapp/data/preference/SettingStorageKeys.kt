package com.devansh.noteapp.data.preference

enum class SettingStorageKeys {
    ACCESS_TOKEN,
    IS_LOGGED_IN,
    AUTO_SYNC_WITH_REMOTE;

    val key get() = this.name
}