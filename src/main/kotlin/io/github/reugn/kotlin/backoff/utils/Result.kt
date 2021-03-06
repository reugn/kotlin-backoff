package io.github.reugn.kotlin.backoff.utils

import kotlinx.serialization.Serializable

/**
 * Result is a type that represents either success [Ok] or failure [Err].
 */
@Serializable
sealed class Result<out T, out E : Exception> {

    abstract val retries: Int

    abstract fun isOk(): Boolean

    abstract fun isErr(): Boolean
}

/**
 * The [Result] that contains the success value.
 */
@Serializable
data class Ok<out T, out E : Exception>(val value: T, override val retries: Int) : Result<T, E>() {
    override fun isOk(): Boolean = true

    override fun isErr(): Boolean = false
}

/**
 * The [Result] that contains the error value.
 */
@Serializable
data class Err<out T, out E : Exception>(val value: E, override val retries: Int) : Result<T, E>() {
    override fun isOk(): Boolean = false

    override fun isErr(): Boolean = true
}
