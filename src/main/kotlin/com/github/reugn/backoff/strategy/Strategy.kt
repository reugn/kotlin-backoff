package com.github.reugn.backoff.strategy

interface Strategy {

    /**
     * Calculate a delay period for the next retry.
     */
    fun next(previousDelayPeriod: Long): Long

    companion object {

        /**
         * A [ConstantStrategy] factory method.
         */
        fun constant(): ConstantStrategy = ConstantStrategy()

        /**
         * An [ExponentialFullJitterStrategy] factory method.
         */
        fun expFullJitter(base: Int): ExponentialFullJitterStrategy = ExponentialFullJitterStrategy(base)

        /**
         * An [ExponentialPartialJitterStrategy] factory method.
         */
        fun expPartialJitter(base: Int, ratio: Double): ExponentialPartialJitterStrategy =
            ExponentialPartialJitterStrategy(base, ratio)

        /**
         * An [ExponentialStrategy] factory method.
         */
        fun exp(base: Int): ExponentialStrategy = ExponentialStrategy(base)
    }
}
