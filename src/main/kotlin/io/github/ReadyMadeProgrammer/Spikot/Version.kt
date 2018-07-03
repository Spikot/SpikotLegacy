package io.github.ReadyMadeProgrammer.Spikot

import kotlin.math.sign

/**
 * Indicate Version
 * @author ReadyMadeProgrammer
 * @since 1.4.0
 */

data class Version(val first: Int, val second: Int, val third: Int, val variation: Variation = Variation.NONE) {
    companion object {
        private val pattern = Regex("([0-9]+)\\.([0-9]+)\\.([0-9]+)([+|\\-])?")
        operator fun invoke(version: String): Version {
            val result = pattern.matchEntire(version)
                    ?: throw IllegalArgumentException("Cannot parse Version from String[$version]")
            val first = result.groupValues[1].toInt()
            val second = result.groupValues[2].toInt()
            val third = result.groupValues[3].toInt()
            val variation = when (result.groupValues[4]) {
                "+" -> Variation.UPPER
                "-" -> Variation.LOWER
                else -> Variation.NONE
            }
            return Version(first, second, third, variation)
        }
    }

    override fun toString() = "$first.$second.$third${variation.code}"
    operator fun compareTo(other: Version) = when {
        first - other.first != 0 -> first - other.first
        second - other.second != 0 -> second - other.second
        else -> third - other.third
    }

    fun match(version: Version): Boolean {
        return when (this.compareTo(version).sign) {
            -1 -> version.variation == Variation.LOWER || this.variation == Variation.UPPER
            1 -> version.variation == Variation.UPPER || this.variation == Variation.LOWER
            else -> true
        }
    }

    enum class Variation(val code: String) {
        NONE(""), UPPER("+"), LOWER("-")
    }
}

/**
 * Version include server platform
 * @author ReadyMadeProgrammer
 * @since 2.0.0
 */
data class ServerVersion(val platform: String, val version: Version) {
    override fun toString() = "$platform-$version"
    fun match(platform: Array<String>, version: Array<String>): Result {
        if (!platform.any { this.platform.toLowerCase().equals(it, ignoreCase = true) }) {
            return Result.PLATFORM_ERROR
        }
        if (version.any { this.version.match(Version(it)) }) {
            return Result.COMPACT
        }
        return Result.VERSION_ERROR
    }

    enum class Result {
        PLATFORM_ERROR, VERSION_ERROR, COMPACT
    }
}