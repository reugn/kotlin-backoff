package com.github.reugn.backoff.strategy

import kotlin.random.Random

class ExponentialFullJitterStrategy(private val base: Int) : Strategy {

    override fun next(delay: Long): Long {
        val next = delay * base
        val jitter = Random.nextInt((next / 2).toInt(), next.toInt())
        return next + jitter
    }
}
