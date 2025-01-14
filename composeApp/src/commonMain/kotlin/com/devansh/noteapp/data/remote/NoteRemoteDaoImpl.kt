package com.devansh.noteapp.data.remote

import co.touchlab.kermit.Logger
import com.devansh.noteapp.data.entity.ServerError
import com.devansh.noteapp.data.entity.ServerResponse
import com.devansh.noteapp.data.remote.utils.BaseGateway
import com.devansh.noteapp.data.remote.utils.Result
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.repo.NoteRemoteDao
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post

class NoteRemoteDaoImpl(
    private val httpClient: HttpClient
) : NoteRemoteDao, BaseGateway(httpClient) {

    override suspend fun upsert(notes: List<Note>,authToken:String) {
        try {
            executeOrThrow<ServerResponse<String>> {
                post(""){

                }
            }
        }catch (e: Exception) {
            Logger.e("network", e) { "${e.message}" }
        }
    }

    override suspend fun getNotes(authToken:String): Result<ServerResponse<List<Note>>, ServerError> {
        val result = tryToExecute<ServerResponse<List<Note>>> {
            get(""){

            }
        }
        return result
    }

    override suspend fun deleteNote(id: Long,authToken:String) {
        try {
            executeOrThrow<ServerResponse<String>> {
                delete("") {

                }
            }
        } catch (e: Exception) {
            Logger.e("network", e) { "${e.message}" }
        }
    }
}