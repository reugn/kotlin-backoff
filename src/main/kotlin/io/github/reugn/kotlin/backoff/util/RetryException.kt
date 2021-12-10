package io.github.reugn.kotlin.backoff.util

/**
 * Thrown to indicate that a retry has failed.
 */
class RetryException : Exception {

    constructor(message: String) : super(message)

    constructor(e: Throwable) : super(e)
}
