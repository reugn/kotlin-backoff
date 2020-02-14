package com.github.reugn.backoff.utils

typealias Success<T> = (T) -> Boolean

/**
 * Success for any condition
 */
val forall: (Any) -> Boolean = { _ -> true }