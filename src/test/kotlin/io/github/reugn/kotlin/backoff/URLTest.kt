package io.github.reugn.kotlin.backoff

import io.github.reugn.kotlin.backoff.strategy.Strategy
import io.github.reugn.kotlin.backoff.utils.nonFatal
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.Duration

class URLTest {

    private val action = suspend { URL("http://worldclockapi.com/api/json/utc/now").readText() }

    @Test
    fun urlTest() {
        val backoff = StrategyBackoff<String>(
            Duration.ofMillis(500), { s -> s.isNotEmpty() }, 3,
            Strategy.expFullJitter(2), ::nonFatal
        )
        val result = runBlocking { backoff.retry(action) }
        //println((result as Ok).value)
        assert(result.isOk())
        Assertions.assertEquals(result.retries, 1)
    }
}
