package com.github.reugn.backoff.strategy

class ExponentialStrategy(private val base: Int) : Strategy {

    override fun next(delay: Long): Long = delay * base
}
