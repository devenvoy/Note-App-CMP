package com.devansh.noteapp.data.repository

import com.devansh.noteapp.data.models.dto.NoteResponse


class SearchNotes {
    fun execute(notes: List<NoteResponse>, query: String): List<NoteResponse> {
        if (query.isEmpty()) {
            return notes
        }
        return notes.filter {
            it.title.trim().lowercase().contains(query.lowercase()) ||
                    it.content.trim().lowercase().contains(query.lowercase())
        }
//            .sortedBy {            DateTimeUtil.toEpochMillis(it.lastModified)        }
    }
}