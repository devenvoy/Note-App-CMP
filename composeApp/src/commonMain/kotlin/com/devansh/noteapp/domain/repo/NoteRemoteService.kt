package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.entity.ServerResponse
import com.devansh.noteapp.domain.model.GetNotesResponse
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.utils.Result

interface NoteRemoteService {
    suspend fun upsert(notes: Note, accessToken: String) : Result<ServerResponse<Note>, ServerError>
    suspend fun getNotes(accessToken: String): Result<ServerResponse<GetNotesResponse>, ServerError>
    suspend fun deleteNote(id: String, accessToken: String)
}