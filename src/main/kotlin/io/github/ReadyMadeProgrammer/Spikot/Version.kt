package io.github.ReadyMadeProgrammer.Spikot

import io.github.ReadyMadeProgrammer.Spikot.Version.Variation.*
import kotlin.math.abs
import kotlin.math.sign

/**
 * Indicate Version
 * @author ReadyMadeProgrammer
 * @since 1.4.0
 */

data class Version(val first: Int, val second: Int, val third: Int, val variation: Variation = NONE) {
    companion object {
        private val pattern = Regex("([0-9]+)\\.([0-9]+)\\.([0-9]+)([+|\\-])?")
        operator fun invoke(version: String): Version {
            val result = pattern.matchEntire(version)
                    ?: throw IllegalArgumentException("Cannot parse Version from String[$version]")
            val first = result.groupValues[1].toInt()
            val second = result.groupValues[2].toInt()
            val third = result.groupValues[3].toInt()
            val variation = when (result.groupValues[4]) {
                "+" -> UPPER
                "-" -> LOWER
                else -> NONE
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
            -1 -> version.variation == LOWER || this.variation == UPPER
            1 -> version.variation == UPPER || this.variation == LOWER
            else -> true
        }
    }

    fun closer(v1: Version, v2: Version): Int {
        val v1M = this.match(v1)
        val v2M = this.match(v2)
        return if (v1M && !v2M) -1
        else if (!v1M && v2M) 1
        else if (!v1M && !v2M) 0
        else when (this.variation) {
            NONE -> {
                (abs(v2.first - first) - abs(v1.first - first)).sign * 100
                +(abs(v2.second - second) - abs(v1.second - second)).sign * 10
                +(abs(v2.third - third) - abs(v1.third - third)).sign
            }
            UPPER -> v1.compareTo(v2)
            LOWER -> v2.compareTo(v1)
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
data class ServerVersion(val platform: Platform, val version: Version) {
    override fun toString() = "$platform-$version"
    fun match(platform: Array<Platform>, version: Array<String>): Result {
        if (!platform.any { this.platform == it }) {
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

    enum class Platform {
        GLOWSTONE, PAPER, SPIGOT, CRAFT, UNKNOWN
    }
}