package com.github.reugn.backoff

import com.github.reugn.backoff.strategy.Strategy
import com.github.reugn.backoff.utils.*
import kotlinx.coroutines.delay
import java.time.Duration

/**
 * Strategy based Backoff implementation
 *
 * @param T the type of retry result
 * @property delayTime base delay interval
 * @property success retry method success condition
 * @property max max number of retries
 * @property strategy next delay time calculation Strategy
 */
class StrategyBackoff<T>(
    private val delayTime: Duration,
    val success: Success<T>,
    private val max: Int = 3,
    private val strategy: Strategy = Strategy.expFullJitter(2)
) : Backoff<T>, Strategy by strategy {

    override suspend fun retry(f: suspend () -> T): Result<T, Exception> {
        return try {
            retryN(f, 1, delayTime.toMillis())
        } catch (e: Exception) {
            Err(e)
        }
    }

    private suspend fun retryN(f: suspend () -> T, n: Int, prev: Long): Result<T, Exception> {
        while (true) {
            return try {
                delay(prev)
                val res = f()
                if (success(res))
                    Ok(res)
                else
                    checkCondition(f, n + 1, next(prev))
            } catch (e: Exception) {
                checkCondition(f, n + 1, next(prev))
            }
        }
    }

    private suspend fun checkCondition(f: suspend () -> T, n: Int, next: Long): Result<T, Exception> {
        return if (n <= max)
            retryN(f, n, next)
        else
            Err(RetryException("Retry limit of $max reached"))
    }

}
