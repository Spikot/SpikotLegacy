package io.github.ReadyMadeProgrammer.Spikot.modules

import com.google.gson.JsonParser
import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import org.bukkit.configuration.file.YamlConfiguration
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import org.koin.standalone.inject
import java.io.File

@Suppress("unused")
@ExternalModule
class FileModule : ModuleConfig {
    override val module: Module
        get() = applicationContext {
            factory { params -> File(spikotPlugin.dataFolder.parent, params.get<String>("path")) }
            factory { params -> JsonParser().parse(File(spikotPlugin.dataFolder.parent, params.get<String>("path")).readText()) }
            factory { params -> YamlConfiguration.loadConfiguration(File(spikotPlugin.dataFolder.parent, params.get<String>("path"))) }
        }
}

fun Component.injectFile(path: String) = inject<File> { mapOf("path" to path) }
