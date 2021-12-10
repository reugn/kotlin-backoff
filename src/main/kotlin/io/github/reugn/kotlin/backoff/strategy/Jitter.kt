package io.github.reugn.kotlin.backoff.strategy

import kotlin.random.Random

/**
 * Represents the jitter trait for backoff strategies.
 */
interface Jitter {

    /**
     * Applies jitter to the delay interval using the specified relative factor.
     */
    fun withJitter(delayInterval: Long, jitterFactor: Double): Long {
        require(jitterFactor in 0.0..1.0)

        val from = (delayInterval * (1 - jitterFactor)).toLong()
        if (from == delayInterval) {
            return delayInterval
        }

        return Random.nextLong(
            from,
            delayInterval
        )
    }
}
