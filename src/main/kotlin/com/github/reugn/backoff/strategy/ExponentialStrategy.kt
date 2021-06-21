package com.github.reugn.backoff.strategy

class ExponentialStrategy(private val base: Int) : Strategy {

    override fun next(previousDelayPeriod: Long): Long = previousDelayPeriod * base
}
