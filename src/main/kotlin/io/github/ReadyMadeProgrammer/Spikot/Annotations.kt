package io.github.ReadyMadeProgrammer.Spikot

@Suppress("unused")
annotation class Beta(val level: BetaLevel)

enum class BetaLevel {
    /**
     * Didn't complete developing. Cannot use some feature and unstable
     */
    DEVELOPING,
    /**
     * Complete developing but api is not tested and unstable.
     */
    UNSTABLE,
    /**
     * Api can change or remove.
     */
    EXPERIMENT
}