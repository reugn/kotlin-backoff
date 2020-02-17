# kotlin-backoff
[![Download](https://api.bintray.com/packages/reug/maven/kotlin-backoff/images/download.svg)](https://bintray.com/reug/maven/kotlin-backoff/_latestVersion)
[![Build Status](https://travis-ci.com/reugn/kotlin-backoff.svg?branch=master)](https://travis-ci.com/reugn/kotlin-backoff)

Simple Kotlin Exponential backoff library

## Installation
Gradle:
```kotlin
repositories {
    maven {
        setUrl("https://dl.bintray.com/reug/maven")
    }
}

dependencies {
    implementation 'com.github.reugn:kotlin-backoff:<version>'
}
```

## Examples
```kotlin
private val action = suspend { URL("http://worldtimeapi.org/").readText() }

@Test
fun urlTest() {
    val backoff = StrategyBackoff<String>(Duration.ofMillis(100), { s -> s.isNotEmpty() }, 3,
        Strategy.expFullJitter(2), ::nonFatal)
    val result = runBlocking { backoff.retry(action) }
    assert(result.isOk())
    assertEquals(result.retries, 1)
}
```

## License
Licensed under the [Apache 2.0 License](./LICENSE).