package io.github.reugn.kotlin.backoff.util

typealias ErrorValidator = (Throwable) -> Boolean

/**
 * Returns true if the provided Throwable is to be considered non-fatal,
 * or false if it is to be considered fatal.
 */
fun nonFatal(e: Throwable): Boolean {
    return when (e) {
        is VirtualMachineError, is ThreadDeath, is InterruptedException, is LinkageError -> false
        else -> true
    }
}
