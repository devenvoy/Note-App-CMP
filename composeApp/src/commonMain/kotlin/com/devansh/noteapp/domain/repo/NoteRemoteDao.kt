package com.devansh.noteapp.domain.repo

import com.devansh.noteapp.data.entity.ServerError
import com.devansh.noteapp.data.entity.ServerResponse
import com.devansh.noteapp.data.remote.utils.Result
import com.devansh.noteapp.domain.model.GetNotesResponse
import com.devansh.noteapp.domain.model.Note

interface NoteRemoteDao {
    suspend fun upsert(notes : List<Note>,authToken:String)
    suspend fun getNotes(authToken:String):Result<ServerResponse<GetNotesResponse>, ServerError>
    suspend fun deleteNote(id:Long,authToken:String)
}