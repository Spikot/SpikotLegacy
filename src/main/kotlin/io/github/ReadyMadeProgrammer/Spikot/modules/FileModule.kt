package io.github.ReadyMadeProgrammer.Spikot.modules

import com.google.gson.JsonParser
import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import org.bukkit.configuration.file.YamlConfiguration
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.standalone.inject
import java.io.File

@Suppress("unused")
@ExternalModule
class FileModule : ModuleConfig {
    override val module: Module
        get() = module {
            factory { p -> File(spikotPlugin.dataFolder.parent, p[0]) }
            factory { p -> JsonParser().parse(File(spikotPlugin.dataFolder.parent, p[0]).readText()) }
            factory { p -> YamlConfiguration.loadConfiguration(File(spikotPlugin.dataFolder.parent, p[0])) }
        }
}

fun Component.injectFile(path: String) = inject<File> { parametersOf(path) }
