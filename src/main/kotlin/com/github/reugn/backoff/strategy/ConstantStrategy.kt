package com.github.reugn.backoff.strategy

class ConstantStrategy() : Strategy {

    override fun next(delay: Long): Long = delay
}
