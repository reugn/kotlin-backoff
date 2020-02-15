package com.github.reugn.backoff.strategy

interface Strategy {

    /**
     * Calculate suspend time based on previous delay
     */
    fun next(delay: Long): Long

    companion object {

        /**
         * [ConstantStrategy] factory method
         */
        fun constant(): ConstantStrategy = ConstantStrategy()

        /**
         * [ExponentialFullJitterStrategy] factory method
         */
        fun expFullJitter(base: Int): ExponentialFullJitterStrategy = ExponentialFullJitterStrategy(base)

        /**
         * [ExponentialPartialJitterStrategy] factory method
         */
        fun expPartialJitter(base: Int, ratio: Double): ExponentialPartialJitterStrategy =
            ExponentialPartialJitterStrategy(base, ratio)

        /**
         * [ExponentialStrategy] factory method
         */
        fun exp(base: Int): ExponentialStrategy = ExponentialStrategy(base)
    }
}
