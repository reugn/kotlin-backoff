package io.github.reugn.kotlin.backoff.strategy

class ConstantStrategy : Strategy {

    override fun next(previousDelayPeriod: Long): Long = previousDelayPeriod
}
