package com.devansh.noteapp.data.remote

import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.repo.NoteService
import com.devansh.noteapp.domain.utils.BaseGateway
import com.devansh.noteapp.domain.utils.Result
import com.jignesh.society.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class NoteServiceImpl(
    private val httpClient: HttpClient
) : NoteService, BaseGateway(httpClient) {

    override suspend fun upsert(
        notes: Note,
        accessToken: String
    ): Result<Note, ServerError> {
        return tryToExecute<Note> {
            post(BuildConfig.BASE_URL + "/notes") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                setBody(notes)
            }
        }
    }

    override suspend fun getNotes(accessToken: String): Result<List<Note>, ServerError> {
        return tryToExecute<List<Note>> {
            get(BuildConfig.BASE_URL + "/notes") {
                header(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }
    }


    override suspend fun deleteNote(id: String, accessToken: String) {
        try {
            executeOrThrow<Unit> {
                delete(BuildConfig.BASE_URL + "/notes/$id") {
                    header(HttpHeaders.Authorization, "Bearer $accessToken")
                }
            }
        } catch (e: Exception) {
            Logger.SIMPLE.log("network ${e.message}")
        }
    }
}