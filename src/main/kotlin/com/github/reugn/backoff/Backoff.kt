package com.github.reugn.backoff

import com.github.reugn.backoff.utils.Result

interface Backoff<T> {

    suspend fun retry(f: suspend () -> T): Result<T, Exception>
}
