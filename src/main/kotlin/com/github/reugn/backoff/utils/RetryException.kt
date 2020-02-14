package com.github.reugn.backoff.utils

class RetryException : Exception {

    constructor() : super()

    constructor(msg: String) : super(msg)

    constructor(msg: String, e: Exception) : super(msg, e)
}
