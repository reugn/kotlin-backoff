package com.github.reugn.backoff

import com.github.reugn.backoff.strategy.Strategy
import com.github.reugn.backoff.utils.Ok
import com.github.reugn.backoff.utils.forall
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.time.Duration
import kotlin.system.measureTimeMillis

class StrategyBackoffTest {

    @Test
    fun exponentialDefaultStrategy() {
        val backoff = StrategyBackoff<Int>(Duration.ofMillis(100), { i -> i == 1 })
        val t = measureTimeMillis {
            when (val v = runBlocking { backoff.retry { 1 } }) {
                is Ok -> {
                    assertEquals(v.value, 1)
                    assertEquals(v.retries, 1)
                }
                else -> fail("Error returned")
            }
        }
        println("exponentialDefaultStrategy time: $t")
        assert(t < 250)
    }

    @Test
    fun constantStrategy() {
        val backoff = StrategyBackoff<Int>(Duration.ofMillis(100), forall, 2, Strategy.constant())
        val result = runBlocking { backoff.retry { 1 } }
        assert(result.isOk())
        assertEquals(result.retries, 1)
    }

    @Test
    fun exponentialDefaultStrategyErr() {
        val backoff = StrategyBackoff<Int>(Duration.ofMillis(100), { i -> i == 1 })
        val result = runBlocking { backoff.retry { 2 } }
        assert(result.isErr())
        assertEquals(result.retries, 3)
    }

    @Test
    fun invalidThrowableErr() {
        val backoff = StrategyBackoff<Int>(Duration.ofMillis(100), { i -> i == 1 })
        val result = runBlocking { backoff.retry { throw InterruptedException() } }
        assert(result.isErr())
        assertEquals(result.retries, 1)
    }

    @Test
    fun validThrowableErr() {
        val backoff = StrategyBackoff<Int>(Duration.ofMillis(100), { i -> i == 1 })
        val result = runBlocking { backoff.retry { throw Exception() } }
        assert(result.isErr())
        assertEquals(result.retries, 3)
    }
}
