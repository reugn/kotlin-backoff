package io.github.reugn.kotlin.backoff

import io.github.reugn.kotlin.backoff.strategy.ExponentialStrategy
import io.github.reugn.kotlin.backoff.util.Err
import io.github.reugn.kotlin.backoff.util.Ok
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.system.measureTimeMillis

class ExponentialStrategyTest {

    @Test
    fun `Exponential strategy no jitter`() {
        val backoff = StrategyBackoff<Int>(
            strategy = ExponentialStrategy(jitterFactor = 0.0),
            resultValidator = { i -> i == 1 },
        )
        val t = measureTimeMillis {
            when (val v = runBlocking { backoff.retry { 1 } }) {
                is Ok -> {
                    assertEquals(1, v.value)
                    assertEquals(1, v.retries)
                }
                else -> {
                    fail("Retry failed")
                }
            }
        }
        assert(t < 300)
    }

    @Test
    fun `Exponential strategy next interval`() {
        val strategy = ExponentialStrategy(jitterFactor = 0.0)
        var interval = 0L
        for (attempt in 1..5) {
            interval = strategy.nextDelay(attempt)
        }
        assertEquals(3200, interval)
    }

    @Test
    fun `Exponential strategy retryable error`() {
        val backoff = StrategyBackoff<Int>(
            maxRetries = 2,
            strategy = ExponentialStrategy(),
        )
        when (val v = runBlocking { backoff.retry { throw Exception() } }) {
            is Err -> {
                assert(v.value is Exception)
                assertEquals(2, v.retries)
            }
            else -> {
                fail("Retry succeed")
            }
        }
    }

    @Test
    fun `Exponential strategy unretryable error`() {
        val backoff = StrategyBackoff<Int>(
            maxRetries = 4,
            strategy = ExponentialStrategy(),
        )
        when (val v = runBlocking { backoff.retry { throw InterruptedException() } }) {
            is Err -> {
                assert(v.value is InterruptedException)
                assertEquals(1, v.retries)
            }
            else -> {
                fail("Retry succeed")
            }
        }
    }

    @Test
    fun `Exponential strategy with jitter`() {
        val backoff = StrategyBackoff<Int>(
            strategy = ExponentialStrategy(jitterFactor = 0.5),
            resultValidator = { i -> i == 1 },
        )
        val t = measureTimeMillis {
            when (val v = runBlocking { backoff.retry { 1 } }) {
                is Ok -> {
                    assertEquals(1, v.value)
                    assertEquals(1, v.retries)
                }
                else -> {
                    fail("Retry failed")
                }
            }
        }
        assert(t < 250)
    }
}
