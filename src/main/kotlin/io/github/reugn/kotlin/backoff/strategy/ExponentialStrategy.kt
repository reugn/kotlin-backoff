package io.github.reugn.kotlin.backoff.strategy

class ExponentialStrategy(private val base: Int) : Strategy {

    override fun next(previousDelayPeriod: Long): Long = previousDelayPeriod * base
}
