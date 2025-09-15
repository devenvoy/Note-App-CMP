package com.devansh.noteapp.domain.utils


sealed interface Result<out D, out E: Error> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data object Loading : Result<Nothing, Nothing>
    data class Failure<out E: Error>(val error: E):
        Result<Nothing, E>
}

inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Failure -> Result.Failure(error)
        is Result.Success -> Result.Success(map(data))
        Result.Loading -> Result.Loading
    }
}

fun <T, E: Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {  }
}

inline fun <T, E: Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Failure -> this
        is Result.Success -> {
            action(data)
            this
        }

        Result.Loading -> this
    }
}
inline fun <T, E: Error> Result<T, E>.onFailure(action: (E) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Failure -> {
            action(error)
            this
        }
        is Result.Success -> this
        Result.Loading -> this
    }
}

inline fun <T, E : Error> Result<T, E>.onLoading(action: UnitCBF): Result<T, E> {
    return when (this) {
        is Result.Failure -> this
        is Result.Success -> this
        Result.Loading -> {
            action()
            this
        }
    }
}

typealias EmptyResult<E> = Result<Unit, E>