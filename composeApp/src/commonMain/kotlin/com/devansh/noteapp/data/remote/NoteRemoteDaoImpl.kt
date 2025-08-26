package com.devansh.noteapp.data.remote

import com.devansh.noteapp.data.entity.ServerError
import com.devansh.noteapp.data.entity.ServerResponse
import com.devansh.noteapp.data.remote.utils.BaseGateway
import com.devansh.noteapp.data.remote.utils.Result
import com.devansh.noteapp.domain.model.GetNotesResponse
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.repo.NoteRemoteDao
import com.jignesh.society.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.math.log

class NoteRemoteDaoImpl(
    private val httpClient: HttpClient
) : NoteRemoteDao, BaseGateway(httpClient) {

    override suspend fun upsert(notes: List<Note>,authToken:String) {
        try {
            executeOrThrow<ServerResponse<String>> {
                post(BuildConfig.BASE_URL+"/notes/insert"){
                    contentType(ContentType.Application.Json)
                    header("Authorization", "Bearer $authToken")
                    setBody(notes)
                }
            }
        }catch (e: Exception) {
            Logger.SIMPLE.log("network ${e.message}")
        }
    }

    override suspend fun getNotes(authToken: String): Result<ServerResponse<GetNotesResponse>, ServerError> {
        val result = tryToExecute<ServerResponse<GetNotesResponse>> {
            get(BuildConfig.BASE_URL + "/notes/get-all") {
                header("Authorization", "Bearer $authToken")
            }
        }
        return result
    }


    override suspend fun deleteNote(id: Long, authToken: String) {
        try {
            executeOrThrow<ServerResponse<String>> {
                delete(BuildConfig.BASE_URL + "/notes/delete/$id") {
                    header("Authorization", "Bearer $authToken")
                }
            }
        } catch (e: Exception) {
            Logger.SIMPLE.log("network ${e.message}")
        }
    }
}