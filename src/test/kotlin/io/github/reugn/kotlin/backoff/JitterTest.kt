package io.github.reugn.kotlin.backoff

import io.github.reugn.kotlin.backoff.strategy.Jitter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JitterTest {

    private val jitterObject = object : Jitter {}

    @Test
    fun `Low jitter bound`() {
        assertEquals(1000L, jitterObject.withJitter(1000L, 0.0))
    }

    @Test
    fun `High jitter bound`() {
        for (i in 1..10) {
            val interval = jitterObject.withJitter(1000L, 1.0)
            assert(interval in 0L..1000L)
        }
    }

    @Test
    fun `Jitter bound`() {
        for (i in 1..10) {
            val interval = jitterObject.withJitter(1000L, 0.1)
            assert(interval in 900L..1000L)
        }
    }
}
