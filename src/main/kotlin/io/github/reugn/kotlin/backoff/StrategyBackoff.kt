package io.github.reugn.kotlin.backoff

import io.github.reugn.kotlin.backoff.strategy.ExponentialStrategy
import io.github.reugn.kotlin.backoff.strategy.Strategy
import io.github.reugn.kotlin.backoff.util.*
import kotlinx.coroutines.delay

/**
 * A [Strategy] based [Backoff] implementation.
 * Backoff strategies calculate the delay that should be applied between retries.
 *
 * @param T the return type of operation to be executed with retries.
 * @property maxRetries the maximum number of retries.
 * @property strategy the next delay time calculation [Strategy].
 *          See implemented strategies in the io.github.reugn.kotlin.backoff.strategy package.
 * @property errorValidator exits the retry loop on an invalid exception.
 * @property resultValidator validates the operation result and returns if successful. Continues to
 *          another retry cycle otherwise.
 */
class StrategyBackoff<T>(
    private val maxRetries: Int = 3,
    private val strategy: Strategy = ExponentialStrategy(),
    private val errorValidator: ErrorValidator = ::nonFatal,
    private val resultValidator: ResultValidator<T> = ::acceptAny,
) : Backoff<T>, Strategy by strategy {

    init {
        require(maxRetries > 0)
    }

    override suspend fun retry(
        operation: suspend () -> T
    ): Result<T, Throwable> {
        return retryWithAttempts(operation, 1)
    }

    override suspend fun withRetries(
        operation: suspend () -> T
    ): Result<T, Throwable> {
        return try {
            val result = operation()
            if (resultValidator(result))
                Ok(result, 0)
            else
                retryWithAttempts(operation, 1)
        } catch (e: Throwable) {
            if (errorValidator(e))
                retryWithAttempts(operation, 1)
            else
                Err(e, 0)
        }
    }

    private suspend fun retryWithAttempts(
        operation: suspend () -> T,
        attempt: Int
    ): Result<T, Throwable> {
        while (true) {
            return try {
                delay(nextDelay(attempt))

                val result = operation()
                if (resultValidator(result))
                    Ok(result, attempt)
                else
                    validateAttempts(operation, attempt + 1)
            } catch (e: Throwable) {
                if (validateThrowable(e))
                    validateAttempts(operation, attempt + 1, e)
                else
                    Err(e, attempt)
            }
        }
    }

    private fun validateThrowable(e: Throwable): Boolean {
        return e !is RetryException && errorValidator(e)
    }

    private suspend fun validateAttempts(
        operation: suspend () -> T,
        attempt: Int,
        e: Throwable? = null
    ): Result<T, Throwable> {
        return if (attempt <= maxRetries) {
            retryWithAttempts(operation, attempt)
        } else {
            e?.let {
                Err(it, maxRetries)
            } ?: Err(RetryException("Result validation failed."), maxRetries)
        }
    }
}
