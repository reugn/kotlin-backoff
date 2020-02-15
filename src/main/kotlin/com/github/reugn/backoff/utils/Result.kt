package com.github.reugn.backoff.utils

import kotlinx.serialization.Serializable

@Serializable
sealed class Result<out T, out E : Exception>() {

    abstract val retries: Int

    abstract fun isOk(): Boolean

    abstract fun isErr(): Boolean
}

@Serializable
data class Ok<out T, out E : Exception>(val value: T, override val retries: Int) : Result<T, E>() {
    override fun isOk(): Boolean = true

    override fun isErr(): Boolean = false
}

@Serializable
data class Err<out T, out E : Exception>(val value: E, override val retries: Int) : Result<T, E>() {
    override fun isOk(): Boolean = false

    override fun isErr(): Boolean = true
}
