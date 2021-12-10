package io.github.reugn.kotlin.backoff.strategy

import io.github.reugn.kotlin.backoff.util.RetryException

/**
 * A strategy that returns the delay time as a fixed value determined by the attempt number.
 */
data class FixedStrategy(

    /**
     * The list of the fixed intervals in milliseconds.
     */
    private val intervalsMs: List<Long>

) : Strategy {

    init {
        require(intervalsMs.isNotEmpty())
    }

    override fun nextDelay(attempt: Int): Long {
        try {
            return intervalsMs.elementAt(attempt - 1)
        } catch (e: IndexOutOfBoundsException) {
            throw RetryException(e)
        }
    }
}
