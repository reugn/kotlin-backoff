package io.github.reugn.kotlin.backoff

import io.github.reugn.kotlin.backoff.strategy.PolynomialStrategy
import io.github.reugn.kotlin.backoff.util.Err
import io.github.reugn.kotlin.backoff.util.Ok
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.system.measureTimeMillis

class PolynomialStrategyTest {

    @Test
    fun `Polynomial strategy no jitter`() {
        val backoff = StrategyBackoff<Int>(
            strategy = PolynomialStrategy(jitterFactor = 0.0),
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
        assert(t < 200)
    }

    @Test
    fun `Polynomial strategy next interval`() {
        val strategy = PolynomialStrategy(jitterFactor = 0.0)
        var interval = 0L
        for (attempt in 1..5) {
            interval = strategy.nextDelay(attempt)
        }
        assertEquals(2500, interval)
    }

    @Test
    fun `Polynomial strategy retryable error`() {
        val backoff = StrategyBackoff<Int>(
            maxRetries = 2,
            strategy = PolynomialStrategy(),
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
    fun `Polynomial strategy unretryable error`() {
        val backoff = StrategyBackoff<Int>(
            maxRetries = 4,
            strategy = PolynomialStrategy(),
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
    fun `Polynomial strategy with jitter`() {
        val backoff = StrategyBackoff<Int>(
            strategy = PolynomialStrategy(jitterFactor = 0.5),
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
        assert(t < 150)
    }
}
