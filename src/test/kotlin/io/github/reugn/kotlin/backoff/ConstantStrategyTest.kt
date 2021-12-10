package io.github.reugn.kotlin.backoff

import io.github.reugn.kotlin.backoff.strategy.ConstantStrategy
import io.github.reugn.kotlin.backoff.util.Err
import io.github.reugn.kotlin.backoff.util.Ok
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.system.measureTimeMillis

class ConstantStrategyTest {

    @Test
    fun `Constant strategy`() {
        val backoff = StrategyBackoff<Int>(
            strategy = ConstantStrategy(),
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
    fun `Constant strategy next interval`() {
        val strategy = ConstantStrategy()
        var interval = 0L
        for (attempt in 1..4) {
            interval = strategy.nextDelay(attempt)
        }
        assertEquals(100, interval)
    }

    @Test
    fun `Constant strategy failure`() {
        val backoff = StrategyBackoff<Int>(
            strategy = ConstantStrategy(),
        )
        val t = measureTimeMillis {
            when (val v = runBlocking { backoff.retry { throw Exception() } }) {
                is Err -> {
                    assert(v.value is Exception)
                    assertEquals(3, v.retries)
                }
                else -> {
                    fail("Retry succeed")
                }
            }
        }
        assert(t < 450)
    }

    @Test
    fun `Constant strategy with jitter`() {
        val backoff = StrategyBackoff<Int>(
            strategy = ConstantStrategy(jitterFactor = 0.5),
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
