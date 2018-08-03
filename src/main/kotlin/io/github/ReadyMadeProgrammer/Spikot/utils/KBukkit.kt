package io.github.ReadyMadeProgrammer.Spikot.utils

import io.github.ReadyMadeProgrammer.Spikot.ServerVersion
import io.github.ReadyMadeProgrammer.Spikot.ServerVersion.Platform.*
import io.github.ReadyMadeProgrammer.Spikot.Version
import org.bukkit.Bukkit

@Suppress("unused")
val version = findVersion()

private fun findVersion(): ServerVersion {
    val platform = when { //Code from skript
        classExists("net.glowstone.GlowServer") -> GLOWSTONE
        classExists("co.aikar.timings.Timings") -> PAPER
        classExists("org.spigotmc.SpigotConfig") -> SPIGOT
        classExists("org.bukkit.craftbukkit.CraftServer") || classExists("org.bukkit.craftbukkit.main") -> CRAFT
        else -> UNKNOWN
    }
    val version = Bukkit.getBukkitVersion()
    val splitted = version.split('.')
    return if (splitted.size == 2) {
        ServerVersion(platform, Version(splitted[0].toInt(), splitted[1].toInt(), 0))
    } else {
        ServerVersion(platform, Version(splitted[0].toInt(), splitted[1].toInt(), splitted[3].toIntOrNull() ?: 0))
    }
}

private fun classExists(name: String) = try {
    Class.forName(name)
    true
} catch (e: ClassNotFoundException) {
    false
}

