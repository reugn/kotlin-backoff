package com.github.reugn.backoff.strategy

import kotlin.random.Random

class ExponentialFullJitterStrategy(private val base: Int) : Strategy {

    override fun next(delay: Long): Long {
        val next = delay * base
        val jitter = Random.nextInt(0, (next / 2).toInt())
        return next + jitter
    }
}
