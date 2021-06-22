package io.github.reugn.kotlin.backoff.utils

typealias Success<T> = (T) -> Boolean

/**
 * Succeed for any condition.
 */
val forall: (Any) -> Boolean = { _ -> true }
