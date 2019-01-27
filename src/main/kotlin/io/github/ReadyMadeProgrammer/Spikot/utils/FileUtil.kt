package io.github.ReadyMadeProgrammer.Spikot.utils

import org.bukkit.plugin.Plugin
import java.io.File

fun Plugin.getFile(path: String, file: String): File {
    return File(File(dataFolder, path), file)
}

fun Plugin.getFile(file: String): File {
    return File(dataFolder, file)
}

fun Plugin.createFile(path: String, file: String): File {
    val dir = File(dataFolder, path)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    val final = File(dir, file)
    final.createNewFile()
    return final
}

fun Plugin.createFile(file: String): File {
    if (!dataFolder.exists()) {
        dataFolder.mkdirs()
    }
    val final = File(dataFolder, file)
    final.createNewFile()
    return final
}