package io.github.reugn.kotlin.backoff.strategy

/**
 * This is the base interface type for all backoff strategies.
 */
interface Strategy {

    /**
     * Calculates and returns a delay interval for the next retry.
     */
    fun nextDelay(attempt: Int): Long
}
