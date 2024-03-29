package io.github.reugn.kotlin.backoff.strategy

import java.lang.Long.min
import kotlin.math.pow

/**
 * A strategy in which the next delay interval is calculated using
 * `baseDelayMs * expBase.pow(attempt)` where:
 *  - baseDelayMs is the base delay in milliseconds.
 *  - attempt is the number of unsuccessful attempts that have been made.
 *  - expBase is the exponent base configured for the strategy.
 *
 * The specified jitter and scale factor are applied to the calculated interval.
 * The delay time cannot exceed the specified maximum delay in milliseconds.
 */
data class ExponentialStrategy(

    /**
     * The base delay in milliseconds.
     */
    private val baseDelayMs: Long = 100L,

    /**
     * The maximum delay in milliseconds.
     */
    private val maxDelayMs: Long = 10000L,

    /**
     * The exponent base.
     */
    private val expBase: Int = 2,

    /**
     * The relative jitter factor applied to the interval.
     * Specify 1.0 for full jitter, 0.0 for no jitter.
     */
    private val jitterFactor: Double = 0.1,

    /**
     * The scale factor by which to multiply the calculated interval.
     */
    private val scaleFactor: Double = 1.0

) : Strategy, Jitter {

    init {
        require(baseDelayMs > 0)
        require(maxDelayMs > 0)
        require(expBase > 0)
        require(jitterFactor in 0.0..1.0)
        require(scaleFactor > 0)
    }

    override fun nextDelay(
        attempt: Int,
    ): Long {
        val expInterval = (baseDelayMs * expBase.toDouble().pow(attempt.toDouble())).toLong()
        return min(
            maxDelayMs,
            (withJitter(expInterval, jitterFactor) * scaleFactor).toLong()
        )
    }
}
