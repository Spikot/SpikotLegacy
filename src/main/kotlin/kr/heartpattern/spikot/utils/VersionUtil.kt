package kr.heartpattern.spikot.utils

import kr.heartpattern.spikot.misc.Interval
import kr.heartpattern.spikot.misc.UnboundInterval
import net.swiftzer.semver.SemVer
import org.bukkit.Server
import java.util.*
import java.util.regex.Pattern

private val versionIntervalRegex = Pattern.compile("(([(\\[])(\\S+))?(\\s*~\\s*)((\\S+)([)\\]]))?")
private val nmsVersionRegex = Pattern.compile("org\\.bukkit\\.craftbukkit\\.v(S+)")

/**
 * Parse version interval. Version interval can be both-side-bounded or one-side-bounded.
 * Example of both-side-bounded version interval is "[1.5.2 ~ 1.12.2)" which means equals or large than 1.5.2, less than
 * 1.12.2
 * Example of one-side-bounded version interval is "(1.5.2 ~ " which means large than 1.5.2, or "~ 1.12.2]" which means
 * equals or less than 1.12.2
 * @param version Version interval string
 * @return Parsed version interval
 */
fun parseVersionInterval(version: String): Interval<SemVer> {
    if (version == "all")
        return Interval(UnboundInterval.NegativeInfinite(), UnboundInterval.PositiveInfinite())
    val matcher = versionIntervalRegex.matcher(version)
    val result = matcher.matches()
    require(result) { "Malformed version" }
    val startVersion = when (matcher.group(2)) {
        null -> UnboundInterval.NegativeInfinite()
        "(" -> UnboundInterval.Open(SemVer.parse(matcher.group(3)))
        "[" -> UnboundInterval.Close(SemVer.parse(matcher.group(3)))
        else -> error("Malformed version")
    }

    val endVersion = when (matcher.group(7)) {
        null -> UnboundInterval.PositiveInfinite()
        ")" -> UnboundInterval.Open(SemVer.parse(matcher.group(6)))
        "]" -> UnboundInterval.Close(SemVer.parse(matcher.group(6)))
        else -> error("Malformed version")
    }

    return Interval(startVersion, endVersion)
}

/**
 * NMS version of server
 */
val Server.NMSVersion: String
    get() {
        val server = this::class.java
        val matcher = nmsVersionRegex.matcher(server.`package`.name)
        require(matcher.matches()) { "Cannot find bukkit version" }
        return matcher.group(1)
    }

/**
 * Minecraft version of server in SemVer format
 */
val Server.minecraftSemVer: SemVer
    get() = SemVer.parse(version)

/**
 * Bukkit version of server in SemVer format
 */
val Server.bukkitSemVer: SemVer
    get() = SemVer.parse(bukkitVersion)

/**
 * NMS version of server in SemVer format
 */
val Server.NMSSemVer: SemVer
    get() {
        val version = NMSVersion
        val scanner = Scanner(version)
        scanner.useDelimiter("_")
        val numbers = mutableListOf<Int>()
        val strings = mutableListOf<String>()
        while (scanner.hasNextInt())
            numbers.add(scanner.nextInt())

        while (scanner.hasNext())
            strings.add(scanner.next())

        return if (numbers.size == 2)
            SemVer(numbers[0], numbers[1], 0, strings.joinToString("_"))
        else
            SemVer(numbers[0], numbers[1], numbers[2], strings.joinToString("_"))
    }