package io.github.reugn.kotlin.backoff

import io.github.reugn.kotlin.backoff.strategy.Strategy
import io.github.reugn.kotlin.backoff.utils.*
import kotlinx.coroutines.delay
import java.time.Duration

/**
 * A Strategy based [Backoff] implementation.
 *
 * @param T the type of the retry result.
 * @property delayTime the initial delay interval.
 * @property success the retry method [Success] determiner.
 * @property max the maximum number of retries.
 * @property strategy the next delay time calculation [Strategy].
 * @property validate validate and exit the retry loop on an invalid exception.
 */
class StrategyBackoff<T>(
    private val delayTime: Duration,
    val success: Success<T>,
    private val max: Int = 3,
    private val strategy: Strategy = Strategy.expFullJitter(2),
    private val validate: (Throwable) -> Boolean = ::nonFatal
) : Backoff<T>, Strategy by strategy {

    override suspend fun retry(f: suspend () -> T): Result<T, Exception> {
        return retryN(f, 1, delayTime.toMillis())
    }

    private suspend fun retryN(f: suspend () -> T, n: Int, prev: Long): Result<T, Exception> {
        while (true) {
            return try {
                delay(prev)
                val res = f()
                if (success(res))
                    Ok(res, n)
                else
                    checkCondition(f, n + 1, next(prev))
            } catch (e: Throwable) {
                if (validate(e))
                    checkCondition(f, n + 1, next(prev), e)
                else
                    Err(RetryException(e), n)
            }
        }
    }

    private suspend fun checkCondition(
        f: suspend () -> T, n: Int, next: Long,
        e: Throwable? = null
    ): Result<T, Exception> {
        return if (n <= max)
            retryN(f, n, next)
        else {
            e?.let {
                Err(RetryException(it), max)
            } ?: Err(RetryException(), max)
        }
    }

}
