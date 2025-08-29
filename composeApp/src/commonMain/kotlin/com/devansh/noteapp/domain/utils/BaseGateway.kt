package com.devansh.noteapp.domain.utils

import com.devansh.noteapp.domain.entity.InternetException
import com.devansh.noteapp.domain.entity.ServerError
import com.devansh.noteapp.domain.entity.UnknownErrorException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.serialization.SerializationException

abstract class BaseGateway(val client: HttpClient) {

    suspend inline fun <reified T> executeOrThrow(method: HttpClient.() -> HttpResponse): T {
        try {
            return client.method().body()
        } catch (e: ClientRequestException) {
            val errorMessages = e.response.body<T>()
            throw UnknownErrorException(e.message)
        } catch (e: InternetException.NoInternetException) {
            throw InternetException.NoInternetException()
        } catch (e: Exception) {
            throw UnknownErrorException(e.message.toString())
        }
    }

    suspend inline fun <reified T> tryToExecute(method: HttpClient.() -> HttpResponse): Result<T, ServerError> {
        return try {
            val response: HttpResponse = client.method()
            println("Response: $response \n")
            if (!response.status.isSuccess()) {
                val networkError = response.body<ServerError>()
                println("Response Error: $networkError")
                return Result.Failure(networkError)
            }
            val responseBody: T = response.body()
            println("Response Body: $responseBody")
            Result.Success(responseBody)
        } catch (e: ClientRequestException) {
            val networkError = e.response.body<ServerError>()
            println("Response Error: $networkError")
            Result.Failure(networkError)
        } catch (e: InternetException.NoInternetException) {
            println("Response Error: ${e.message}")
            Result.Failure(
                ServerError(
                    code = 400,
                    value = null,
                    detail = NetworkError.NO_INTERNET.name,
                )
            )
        } catch (e: SerializationException) {
            println("Response Error: ${e.message}")
            Result.Failure(
                ServerError(
                    code = 400,
                    value = null,
                    detail = NetworkError.SERIALIZATION.name,
                )
            )
        } catch (e: Exception) {
            println("Response Error: ${e.message}")
            Result.Failure(
                ServerError(
                    code = 400,
                    value = null,
                    detail = "Unknown Error",
                )
            )
        }
    }


    private fun Map<String, String>.containsErrors(vararg errorCodes: String): Boolean =
        keys.containsAll(errorCodes.toList())

    private fun Map<String, String>.getOrEmpty(key: String): String = get(key) ?: ""

}