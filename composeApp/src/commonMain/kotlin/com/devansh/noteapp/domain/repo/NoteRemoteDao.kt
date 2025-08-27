package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.entity.ServerResponse
import com.devansh.noteapp.domain.utils.Result
import com.devansh.noteapp.domain.model.GetNotesResponse
import com.devansh.noteapp.domain.model.Note

interface NoteRemoteDao {
    suspend fun upsert(notes : List<Note>,authToken:String)
    suspend fun getNotes(authToken:String):Result<ServerResponse<GetNotesResponse>, ServerError>
    suspend fun deleteNote(id:Long,authToken:String)
}