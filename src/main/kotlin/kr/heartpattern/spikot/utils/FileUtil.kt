package kr.heartpattern.spikot.utils

import org.bukkit.plugin.Plugin
import java.io.File

/**
 * Get file inside data folder
 * @param path Path to file
 * @param file Name of file
 * @return File in plugin's data folder
 */
fun Plugin.getFile(path: String, file: String): File {
    return File(File(dataFolder, path), file)
}

/**
 * Get file inside data folder
 * @param file name of file
 * @return File in plugin's data folder
 */
fun Plugin.getFile(file: String): File {
    return File(dataFolder, file)
}

/**
 * Get file and create it inside data folder
 * @param path Path to file
 * @param file Name of file
 * @return File in plugin's data folder
 */
fun Plugin.createFile(path: String, file: String): File {
    val dir = File(dataFolder, path)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    val final = File(dir, file)
    final.createNewFile()
    return final
}


/**
 * Get file and create it inside data folder
 * @param file Name of file
 * @return File in plugin's data folder
 */
fun Plugin.createFile(file: String): File {
    if (!dataFolder.exists()) {
        dataFolder.mkdirs()
    }
    val final = File(dataFolder, file)
    final.createNewFile()
    return final
}