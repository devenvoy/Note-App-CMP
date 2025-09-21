package com.devansh.noteapp.data.repository.repo

import com.devansh.noteapp.core.entity.ServerError
import com.devansh.noteapp.core.utils.Result
import com.devansh.noteapp.data.models.dto.NoteResponse
import com.devansh.noteapp.data.models.dto.request.NoteRequest

interface NoteService {
    suspend fun getNoteResponses(): Result<List<NoteResponse>, ServerError>
    suspend fun upsert(noteResponses: NoteRequest) : Result<NoteResponse, ServerError>
    suspend fun deleteNote(id: String) : Result<Unit, ServerError>
}


