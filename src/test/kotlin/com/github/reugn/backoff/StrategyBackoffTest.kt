package com.github.reugn.backoff

import com.github.reugn.backoff.strategy.Strategy
import com.github.reugn.backoff.utils.forall
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.time.Duration

class StrategyBackoffTest {

    @Test
    fun exponentialDefaultStrategy() {
        val backoff = StrategyBackoff<Int>(Duration.ofMillis(100), { i -> i == 1 })
        val result = runBlocking { backoff.retry { -> 1 } }
        assert(result.isOk())
    }

    @Test
    fun constantStrategy() {
        val backoff = StrategyBackoff<Int>(Duration.ofMillis(100), forall, 2, Strategy.constant(500))
        val result = runBlocking { backoff.retry { -> 1 } }
        assert(result.isOk())
    }

    @Test
    fun exponentialDefaultStrategyErr() {
        val backoff = StrategyBackoff<Int>(Duration.ofMillis(100), { i -> i == 1 })
        val result = runBlocking { backoff.retry { -> 2 } }
        assert(result.isErr())
    }

}
