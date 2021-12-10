package io.github.reugn.kotlin.backoff.strategy

import java.lang.Long.min
import kotlin.math.pow

/**
 * A strategy in which the next delay interval is calculated using
 * `baseDelayMs * attempt.pow(exponent)` where:
 *  - baseDelayMs is the base delay in milliseconds.
 *  - attempt is the number of unsuccessful attempts that have been made.
 *  - exponent is the exponent configured for the strategy.
 *
 * The specified jitter and scale factor are applied to the calculated interval.
 * The delay time cannot exceed the specified maximum delay in milliseconds.
 */
data class PolynomialStrategy(

    /**
     * The base delay in milliseconds.
     */
    private val baseDelayMs: Long = 100L,

    /**
     * The maximum delay in milliseconds.
     */
    private val maxDelayMs: Long = 10000L,

    /**
     * The exponent.
     */
    private val exponent: Int = 2,

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
        require(exponent > 0)
        require(jitterFactor in 0.0..1.0)
        require(scaleFactor > 0)
    }

    override fun nextDelay(
        attempt: Int
    ): Long {
        val polInterval = (baseDelayMs * attempt.toDouble().pow(exponent.toDouble())).toLong()
        return min(
            maxDelayMs,
            (withJitter(polInterval, jitterFactor) * scaleFactor).toLong()
        )
    }
}
