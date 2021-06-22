package io.github.reugn.kotlin.backoff

import io.github.reugn.kotlin.backoff.utils.Result

interface Backoff<T> {

    suspend fun retry(f: suspend () -> T): Result<T, Exception>
}
