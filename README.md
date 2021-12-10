# kotlin-backoff
[![Build](https://github.com/reugn/kotlin-backoff/actions/workflows/build.yml/badge.svg)](https://github.com/reugn/kotlin-backoff/actions/workflows/build.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.reugn/kotlin-backoff/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.reugn/kotlin-backoff/)

Any I/O resource can be temporarily unavailable and cause requests to fail.
Use this library to catch errors and retry, slowing down according to your chosen strategy.
Add validation rules for the result and error types if needed.

## Installation
`kotlin-backoff` is available on Maven Central.  
Add the library as a dependency to your project:
```kotlin
dependencies {
    implementation("io.github.reugn:kotlin-backoff:0.4.0")
}
```

## Backoff strategies
Below is a list of backoff strategies implemented. To create your own strategy, implement the `Strategy` interface.

### Exponential strategy
A strategy in which the next delay interval is calculated using `baseDelayMs * expBase.pow(attempt)` where:
* `baseDelayMs` is the base delay in milliseconds.
* `attempt` is the number of unsuccessful attempts that have been made.
* `expBase` is the exponent base configured for the strategy.

The specified jitter¹ and scale factor² are applied to the calculated interval.
The delay time cannot exceed the specified maximum delay in milliseconds.

### Polynomial strategy
A strategy in which the next delay interval is calculated using `baseDelayMs * attempt.pow(exponent)` where:
* `baseDelayMs` is the base delay in milliseconds.
* `attempt` is the number of unsuccessful attempts that have been made.
* `exponent` is the exponent configured for the strategy.

The specified jitter¹ and scale factor² are applied to the calculated interval.
The delay time cannot exceed the specified maximum delay in milliseconds.

### Fixed strategy
A strategy that returns the delay time as a fixed value determined by the attempt number.

### Constant strategy
A simple backoff strategy that constantly returns the same value.
The specified jitter¹ is applied to the interval. No jitter by default.

---
¹ Jitter adds a retry randomization factor to reduce resource congestion.
Specify 1.0 for full jitter, 0.0 for no jitter.

² The delay determined by the backoff strategy is multiplied by the scale factor. By default, the coefficient is 1.

## Usage example
First, create an instance of `StrategyBackoff`. Then perform the operation, which may fail, using the `retry` or `withRetries` methods.
```kotlin
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
```
For more examples, see the [tests](./src/test/kotlin/io/github/reugn/kotlin/backoff).

## License
Licensed under the [Apache 2.0 License](./LICENSE).
