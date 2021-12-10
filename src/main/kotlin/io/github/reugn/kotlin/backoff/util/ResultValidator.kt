package io.github.reugn.kotlin.backoff.util

typealias ResultValidator<T> = (T) -> Boolean

/**
 * Returns true for any result.
 */
@Suppress("UNUSED_PARAMETER")
fun <T> acceptAny(result: T): Boolean {
    return true
}
