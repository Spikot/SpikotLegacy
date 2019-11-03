package kr.heartpattern.spikot.file

import kr.heartpattern.spikot.module.AbstractModule
import java.io.File

abstract class FileDataController : AbstractModule() {
    protected val directory: File by lazy {
        val root = File(plugin.dataFolder, this::class.qualifiedName!!)
        root.mkdirs()
        root
    }
}