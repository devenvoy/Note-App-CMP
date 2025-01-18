package com.devansh.noteapp.data.preference

enum class SettingStorageKeys {
    ACCESS_TOKEN,
    LIST_TYPE_KEY,
    USER_EMAIL,
    AUTO_SYNC_WITH_REMOTE;

    val key get() = this.name
}