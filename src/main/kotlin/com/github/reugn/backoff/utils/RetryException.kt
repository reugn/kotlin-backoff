package com.github.reugn.backoff.utils

class RetryException : Exception {

    constructor() : super()

    constructor(e: Throwable) : super(e)
}

/**
 * nonFatal Exception matcher
 */
fun nonFatal(e: Throwable): Boolean {
    return when (e) {
        is VirtualMachineError, is ThreadDeath, is InterruptedException, is LinkageError -> false
        else -> true
    }
}
