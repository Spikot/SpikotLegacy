package io.github.ReadyMadeProgrammer.Spikot.reflections

import org.bukkit.Bukkit

object ReflectionUtils {
    @Suppress("MemberVisibilityCanBePrivate")
    val version = Bukkit.getServer().javaClass.`package`.name
            .replace(".", ",").split(",")[3]

    fun getCraftClass(name: String): Class<*> {
        return Class.forName("org.bukkit.craftbukkit.$version.$name")
    }

    fun getNmsClass(name: String): Class<*> {
        return Class.forName("net.minecraft.server.$version.$name")
    }
}