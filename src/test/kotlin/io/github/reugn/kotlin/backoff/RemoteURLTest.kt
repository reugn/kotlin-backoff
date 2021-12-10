package io.github.reugn.kotlin.backoff

import io.github.reugn.kotlin.backoff.strategy.ExponentialStrategy
import io.github.reugn.kotlin.backoff.util.nonFatal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URL

@Suppress("BlockingMethodInNonBlockingContext")
class RemoteURLTest {

    private suspend fun urlAction(): String = withContext(Dispatchers.IO) {
        URL("http://worldclockapi.com/api/json/utc/now").readText()
    }

    @Test
    fun `remote URL`() {
        val backoff = StrategyBackoff<String>(
            maxRetries = 3,
            strategy = ExponentialStrategy(),
            errorValidator = ::nonFatal,
            resultValidator = { s -> s.isNotEmpty() },
        )
        val result = runBlocking { backoff.withRetries(::urlAction) }

        assert(result.isOk())
        assertEquals(result.retries, 0)
    }
}
