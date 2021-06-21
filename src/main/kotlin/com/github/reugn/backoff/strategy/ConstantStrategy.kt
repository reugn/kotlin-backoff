package com.github.reugn.backoff.strategy

class ConstantStrategy : Strategy {

    override fun next(previousDelayPeriod: Long): Long = previousDelayPeriod
}
