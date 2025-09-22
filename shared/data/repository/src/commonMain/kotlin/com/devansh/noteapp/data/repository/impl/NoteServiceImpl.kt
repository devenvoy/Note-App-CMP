package com.devansh.noteapp.data.repository.impl

import com.devansh.noteapp.core.entity.ServerError
import com.devansh.noteapp.core.network.BaseGateway
import com.devansh.noteapp.core.utils.Result
import com.devansh.noteapp.data.models.dto.NoteResponse
import com.devansh.noteapp.data.models.dto.request.NoteRequest
import com.devansh.noteapp.data.repository.repo.NoteService
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class NoteServiceImpl(
    private val httpClient: HttpClient
) : NoteService, BaseGateway(httpClient) {

    override suspend fun getNoteResponses(): Result<List<NoteResponse>, ServerError> {
        return tryToExecute<List<NoteResponse>> { get("/notes") }
    }

    override suspend fun upsert(noteResponses: NoteRequest): Result<NoteResponse, ServerError> {
        return tryToExecute<NoteResponse> { post("/notes") { setBody(noteResponses) } }
    }

    override suspend fun deleteNote(id: String): Result<Unit, ServerError> {
        return tryToExecute<Unit> { delete("/notes/$id") }
    }
}