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
        fun constant(time: Long): ConstantStrategy = ConstantStrategy(time)

        /**
         * [ExponentialFullJitterStrategy] factory method
         */
        fun expFullJitter(base: Int): ExponentialFullJitterStrategy = ExponentialFullJitterStrategy(base)

        /**
         * [ExponentialHalfJitterStrategy] factory method
         */
        fun expHalfJitter(base: Int): ExponentialHalfJitterStrategy = ExponentialHalfJitterStrategy(base)

        /**
         * [ExponentialStrategy] factory method
         */
        fun exp(base: Int): ExponentialStrategy = ExponentialStrategy(base)
    }
}
