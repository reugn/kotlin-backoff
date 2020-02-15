# kotlin-backoff
Simple Kotlin Exponential backoff library

## Examples
```kotlin
val backoff = StrategyBackoff<Int>(Duration.ofMillis(100), { i -> i == 1 })
val result = runBlocking { backoff.retry { -> 1 } }
assert(result.isOk())
assertEquals(result.retries, 1)
```

## License
Licensed under the [Apache 2.0 License](./LICENSE).