package kr.heartpattern.spikot.adapter

import kr.heartpattern.spikot.misc.Interval
import kr.heartpattern.spikot.utils.NMSSemVer
import kr.heartpattern.spikot.utils.bukkitSemVer
import kr.heartpattern.spikot.utils.minecraftSemVer
import kr.heartpattern.spikot.utils.parseVersionInterval
import net.swiftzer.semver.SemVer
import org.bukkit.Bukkit
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * Annotate supported version
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SupportedVersion(
    val version: String,
    vararg val platform: String = []
)

/**
 * Type of version in bukkit
 */
enum class VersionType {
    MINECRAFT,
    BUKKIT,
    NMS
}

/**
 * Adaptor resolver base on version.
 * Select more narrowed version. Narrow of version is determined by whether version interval contains infinite.
 * If both version does not contains infinite, select randomly.
 * @param type Type of adapter interface
 * @param version Type of version
 */
abstract class VersionAdapterResolver<T : IAdapter>(type: KClass<T>, version: VersionType) : AdapterResolver<T>(type) {
    private val currentVersion: SemVer = when (version) {
        VersionType.MINECRAFT -> Bukkit.getServer().minecraftSemVer
        VersionType.BUKKIT -> Bukkit.getServer().bukkitSemVer
        VersionType.NMS -> Bukkit.getServer().NMSSemVer
    }

    override fun canApply(adapter: T): Boolean {
        val platform = getPlatform(adapter)
        return currentVersion in getInterval(adapter) && (platform.isEmpty() || Bukkit.getName() in platform)
    }

    override fun select(a: T, b: T): T {
        val aVersion = getInterval(a)
        val bVersion = getInterval(b)
        val aPlatform = getPlatform(a)
        val bPlatform = getPlatform(b)

        return if (bPlatform.isEmpty() && aPlatform.isNotEmpty()) {
            a
        } else if (aPlatform.isEmpty() && bPlatform.isNotEmpty()) {
            b
        } else if (aVersion in bVersion) {
            a
        } else {
            b
        }
    }

    private fun getInterval(adapter: T): Interval<SemVer> {
        return parseVersionInterval(adapter::class.findAnnotation<SupportedVersion>()!!.version)
    }

    private fun getPlatform(adapter: T): Set<String> {
        return adapter::class.findAnnotation<SupportedVersion>()!!.platform.toSet()
    }
}