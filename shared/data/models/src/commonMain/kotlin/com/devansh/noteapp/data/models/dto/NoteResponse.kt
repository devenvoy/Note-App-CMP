package com.devansh.noteapp.data.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class NoteResponse(
    @Transient val id: Long = -1,
    @SerialName("id") val noteId: String?,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("color") val colorRes: Long?,
    @SerialName("categoryId") val category: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null,

    @Transient val isSynced: Boolean = false,
    @Transient val isDeleted: Boolean = false,
    @Transient val pendingDeletion: Boolean = false,
    val syncAction: SyncAction = SyncAction.NONE
) {

    @OptIn(ExperimentalTime::class)
    fun getUpdatedAtTimestamp(): Long {
        return try {
            if (updatedAt.isNullOrEmpty()) return Clock.System.now().toEpochMilliseconds()
            Instant.parse(updatedAt).toEpochMilliseconds()
        } catch (e: Exception) {
            Clock.System.now().toEpochMilliseconds()
        }
    }

    /**
     * Check if this note needs to be synced
     */
    fun needsSync(): Boolean = !isSynced && !isDeleted && !pendingDeletion

    /**
     * Check if this note needs to be deleted from server
     */
    fun needsDeletion(): Boolean = pendingDeletion && !noteId.isNullOrBlank()

    /**
     * Check if this is a local-only note (never synced)
     */
    fun isLocalOnly(): Boolean = noteId.isNullOrBlank()

    /**
     * Create a copy marked for sync
     */
    fun markForSync(): NoteResponse = copy(isSynced = false, syncAction = SyncAction.UPDATE)

    /**
     * Create a copy marked as synced
     */
    fun markAsSynced(): NoteResponse = copy(isSynced = true, syncAction = SyncAction.NONE)

    /**
     * Create a copy marked for deletion
     */
    fun markForDeletion(): NoteResponse = copy(
        pendingDeletion = true,
        isSynced = false,
        syncAction = SyncAction.DELETE
    )

    /**
     * Create a copy marked as deleted
     */
    fun markAsDeleted(): NoteResponse = copy(
        isDeleted = true,
        pendingDeletion = false,
        isSynced = false
    )

    companion object Companion {
        private val RedOrangeHex = 0xffffab91
        private val RedPinkHex = 0xfff48fb1
        private val BabyBlueHex = 0xff81deea
        private val VioletHex = 0xffcf94da
        private val LightGreenHex = 0xffe7ed9b

        val colors = listOf(
            RedOrangeHex,
            RedPinkHex,
            BabyBlueHex,
            VioletHex,
            LightGreenHex
        )

        fun generateRandomColor() = colors.random()

        fun emptyNote() = NoteResponse(
            noteId = null,
            title = "",
            content = "",
            colorRes = null,
            category = null,
            createdAt = null,
            updatedAt = null
        )
    }
}

enum class SyncAction {
    NONE, CREATE, UPDATE, DELETE
}