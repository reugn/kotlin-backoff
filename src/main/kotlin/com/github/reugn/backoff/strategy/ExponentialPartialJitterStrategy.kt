package com.github.reugn.backoff.strategy

import kotlin.random.Random

class ExponentialPartialJitterStrategy(private val base: Int, private val ratio: Double) : Strategy {

    override fun next(previousDelayPeriod: Long): Long {
        val next = previousDelayPeriod * base
        val jitter = Random.nextInt(0, (previousDelayPeriod / 2).toInt())
        return (next * ratio).toLong() + jitter
    }
}
