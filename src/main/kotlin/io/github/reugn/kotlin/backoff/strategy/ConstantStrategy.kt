package io.github.reugn.kotlin.backoff.strategy

/**
 * A simple backoff strategy that constantly returns the same value.
 * The specified jitter is applied to the interval.
 */
data class ConstantStrategy(

    /**
     * The constant delay interval in milliseconds.
     */
    private val delayMs: Long = 100L,

    /**
     * The relative jitter factor applied to the interval.
     * Specify 1.0 for full jitter, 0.0 for no jitter.
     * No jitter by default.
     */
    private val jitterFactor: Double = 0.0

) : Strategy, Jitter {

    init {
        require(delayMs > 0)
        require(jitterFactor in 0.0..1.0)
    }

    override fun nextDelay(
        attempt: Int
    ): Long {
        return withJitter(delayMs, jitterFactor)
    }
}
