package com.github.reugn.backoff.strategy

import kotlin.random.Random

class ExponentialFullJitterStrategy(private val base: Int) : Strategy {

    override fun next(previousDelayPeriod: Long): Long {
        val next = previousDelayPeriod * base
        val jitter = Random.nextInt(0, (next / 2).toInt())
        return next + jitter
    }
}
