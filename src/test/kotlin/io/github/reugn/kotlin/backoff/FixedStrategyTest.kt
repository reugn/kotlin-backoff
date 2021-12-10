package io.github.reugn.kotlin.backoff

import io.github.reugn.kotlin.backoff.strategy.FixedStrategy
import io.github.reugn.kotlin.backoff.util.Err
import io.github.reugn.kotlin.backoff.util.Ok
import io.github.reugn.kotlin.backoff.util.RetryException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.system.measureTimeMillis

class FixedStrategyTest {

    @Test
    fun `Fixed strategy`() {
        val backoff = StrategyBackoff<Int>(
            strategy = FixedStrategy(listOf(50)),
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
        assert(t < 130)
    }

    @Test
    fun `Fixed strategy next interval`() {
        val intervalList = listOf<Long>(100, 50, 450, 1000)
        val strategy = FixedStrategy(intervalList)
        for (attempt in 1..4) {
            val interval = strategy.nextDelay(attempt)
            assertEquals(intervalList.elementAt(attempt - 1), interval)
        }
    }

    @Test
    fun `Fixed strategy internal error`() {
        val backoff = StrategyBackoff<Int>(
            maxRetries = 5,
            strategy = FixedStrategy(listOf(50, 100)),
            resultValidator = { i -> i == 1 },
        )
        when (val v = runBlocking { backoff.retry { throw Exception() } }) {
            is Err -> {
                assert(v.value is RetryException)
                assertEquals(3, v.retries)
            }
            else -> {
                fail("Retry succeed")
            }
        }
    }
}
