package io.github.ReadyMadeProgrammer.Spikot

import org.bukkit.Bukkit

val version = findVersion()

//TODO: Not implemented
private fun findVersion():Version {
    return Version(0,0,0)
//    val ver = Bukkit.getVersion()
//    val splitted = ver.split(".")
//    return Version(splitted[0].toInt(),splitted[1].toInt(),splitted[2].toInt())
}
