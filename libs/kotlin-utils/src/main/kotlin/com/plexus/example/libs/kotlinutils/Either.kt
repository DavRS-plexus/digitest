package com.plexus.example.libs.kotlinutils

sealed class Either<out S, out F> {
    data class Success<out S>(val data: S) : Either<S, Nothing>()
    data class Failure<out F>(val fail: F) : Either<Nothing, F>()

    companion object {
        fun <S, F> success(data: S): Either<S, F> = Success(data)
        fun <S, F> failure(fail: F): Either<S, F> = Failure(fail)
    }
}

inline fun <S, F, S2, F2> Either<S, F>.map(
    onSuccess: (S) -> S2,
    onFailure: (F) -> F2
): Either<S2, F2> = when (this) {
    is Either.Success -> Either.success(onSuccess(data))
    is Either.Failure -> Either.failure(onFailure(fail))
}