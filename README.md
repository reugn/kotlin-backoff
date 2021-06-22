# kotlin-backoff
[![Build](https://github.com/reugn/kotlin-backoff/actions/workflows/build.yml/badge.svg)](https://github.com/reugn/kotlin-backoff/actions/workflows/build.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.reugn/kotlin-backoff/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.reugn/kotlin-backoff/)

A simple Kotlin Exponential Backoff library designed for `kotlinx.coroutines`.

## Getting started
Gradle:
```kotlin
dependencies {
    implementation("io.github.reugn:kotlin-backoff:<version>")
}
```

## Examples
```kotlin
private val action = suspend { URL("http://worldclockapi.com/api/json/utc/now").readText() }

@Test
fun urlTest() {
    val backoff = StrategyBackoff<String>(Duration.ofMillis(500), { s -> s.isNotEmpty() }, 3,
        Strategy.expFullJitter(2), ::nonFatal)
    val result = runBlocking { backoff.retry(action) }
    assert(result.isOk())
    assertEquals(result.retries, 1)
}
```
More examples can be found in the test section.

## License
Licensed under the [Apache 2.0 License](./LICENSE).
