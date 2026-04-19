package com.example.artistico_android.core.common

/**
 * Domain-level result that never leaks framework exceptions into the UI layer.
 */
sealed interface DomainResult<out T> {
    data class Success<T>(val value: T) : DomainResult<T>
    data class Failure(val error: DomainError) : DomainResult<Nothing>
}

sealed interface DomainError {
    data object Network : DomainError
    data object NotFound : DomainError
    data object Unauthorized : DomainError
    data class Unknown(val message: String? = null) : DomainError
}

inline fun <T, R> DomainResult<T>.map(transform: (T) -> R): DomainResult<R> = when (this) {
    is DomainResult.Success -> DomainResult.Success(transform(value))
    is DomainResult.Failure -> this
}
