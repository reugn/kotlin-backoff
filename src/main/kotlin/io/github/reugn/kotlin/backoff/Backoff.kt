package io.github.reugn.kotlin.backoff

import io.github.reugn.kotlin.backoff.util.Result

interface Backoff<T> {

    /**
     * Retries the specified operation with delays and returns the result.
     */
    suspend fun retry(operation: suspend () -> T): Result<T, Throwable>

    /**
     * Executes the specified operation with retries and returns the result.
     * The difference from [retry] is that the first time the operation is performed without delay.
     */
    suspend fun withRetries(operation: suspend () -> T): Result<T, Throwable>
}
