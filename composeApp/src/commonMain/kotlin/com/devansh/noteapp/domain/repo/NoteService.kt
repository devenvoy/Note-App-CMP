package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.utils.Result

interface NoteService {
    suspend fun upsert(notes: Note, accessToken: String) : Result<Note, ServerError>
    suspend fun getNotes(accessToken: String): Result<List<Note>, ServerError>
    suspend fun deleteNote(id: String, accessToken: String) : Result<Map<String, String>, ServerError>
}