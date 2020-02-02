/*
 * Copyright 2020 HeartPattern
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.adapter

import kr.heartpattern.spikot.misc.Interval
import kr.heartpattern.spikot.module.BaseModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
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
 * @param version Target version. Version is written like (1.5.2 ~ 1.12.2] or ~ 1.5.2).
 * @param platform Target platform. Platform is string that can be obtained by Bukkit.getServer().getPlatform()
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SupportedVersion(
    val version: String,
    vararg val platform: String = []
)

/**
 * Type of version
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
@BaseModule
@Module(priority = ModulePriority.SYSTEM)
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