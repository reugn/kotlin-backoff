package com.github.reugn.backoff.strategy

import kotlin.random.Random

class ExponentialPartialJitterStrategy(private val base: Int, private val ratio: Double) : Strategy {

    override fun next(delay: Long): Long {
        val next = delay * base
        val jitter = Random.nextInt(0, (delay / 2).toInt())
        return (next * ratio).toLong() + jitter
    }
}
