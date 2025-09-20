package com.devansh.noteapp.domain.model

import com.devansh.noteapp.NoteEntity
import com.devansh.noteapp.ui.theme.BabyBlueHex
import com.devansh.noteapp.ui.theme.LightGreenHex
import com.devansh.noteapp.ui.theme.RedOrangeHex
import com.devansh.noteapp.ui.theme.RedPinkHex
import com.devansh.noteapp.ui.theme.VioletHex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class Note(
    @SerialName("id") val id: String?,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("color") val colorRes: Long,
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
    fun needsSync(): Boolean = !isSynced && !isDeleted

    /**
     * Check if this note needs to be deleted from server
     */
    fun needsDeletion(): Boolean = pendingDeletion

    /**
     * Create a copy marked for sync
     */
    fun markForSync(): Note = copy(isSynced = false)

    /**
     * Create a copy marked as synced
     */
    fun markAsSynced(): Note = copy(isSynced = true)

    /**
     * Create a copy marked for update
     */
    fun checkNullStringId(): Note = copy(id = if (id?.contains("null") == true) null else id)

    /**
     * Create a copy marked for deletion
     */
    fun markForDeletion(): Note = copy(pendingDeletion = true, isSynced = false)

    /**
     * Create a copy marked as deleted
     */
    fun markAsDeleted(): Note = copy(isDeleted = true, pendingDeletion = false, isSynced = false)

    companion object {
        val colors = listOf(
            RedOrangeHex,
            RedPinkHex,
            BabyBlueHex,
            VioletHex,
            LightGreenHex
        )

        fun generateRandomColor() = colors.random()

        fun emptyNote() = Note(
            id = null,
            title = "",
            content = "",
            colorRes = generateRandomColor(),
            category = null,
            createdAt = null,
            updatedAt = null
        )
    }
}
fun NoteEntity.toNote() = Note(
    id = id,
    title = title,
    content = content,
    colorRes = colorRes,
    category = categoryId,
    createdAt = createdAt,
    updatedAt = updatedAt
)

enum class SyncAction {
    NONE, CREATE, UPDATE, DELETE
}