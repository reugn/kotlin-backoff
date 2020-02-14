package com.github.reugn.backoff.strategy

class ConstantStrategy(private val time: Long) : Strategy {

    override fun next(delay: Long): Long = time
}
