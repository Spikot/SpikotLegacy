package io.github.ReadyMadeProgrammer.Spikot.config

import io.github.ReadyMadeProgrammer.Spikot.module.*
import io.github.ReadyMadeProgrammer.Spikot.utils.catchAll
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Module(loadOrder = LoadOrder.API - 1000)
object ConfigManager : AbstractModule() {
    private lateinit var root: File
    override fun onEnable() {
        SpikotPluginManager.spikotPlugins.forEach { plugin ->
            root = File(plugin.plugin.dataFolder, "config")
            root.mkdirs()
            plugin.config.filter {
                onDebug {
                    logger.info("Find config: ${it.simpleName}")
                }
                it.canLoad()
            }.forEach { type ->
                catchAll {
                    logger.info("Load config: ${type.simpleName}")
                    val obj = type.objectInstance as? ConfigSpec
                    val annotation = type.findAnnotation<Config>()
                    when {
                        annotation == null -> logger.warn("Cannot load config: ${type.qualifiedName}")
                        obj == null -> logger.warn("Cannot load config: ${type.qualifiedName}")
                        else -> {
                            val file = File(root, "${obj.name ?: type.simpleName}.yml")
                            file.createNewFile()
                            load(type, "", YamlConfiguration.loadConfiguration(file))
                        }
                    }
                }
            }
        }
    }

    override fun onDisable() {
        SpikotPluginManager.spikotPlugins.forEach { plugin ->
            root = File(plugin.plugin.dataFolder, "config")
            root.mkdirs()
            plugin.config.filter { it.canLoad() }.forEach { type ->
                val obj = type.objectInstance as? ConfigSpec
                obj?.yaml?.save(File(root, "${obj.name ?: type.simpleName}.yml"))
            }
        }
    }

    fun load(type: KClass<*>, root: String, source: YamlConfiguration) {
        val obj = type.objectInstance as? ConfigSpec
        if (obj == null) {
            logger.warn("Cannot load config: ${type.qualifiedName}")
            return
        }
        obj.path = root
        obj.yaml = source
        obj.initialize()
        type.nestedClasses.forEach { inner ->
            load(inner, root + ((inner.objectInstance as? ConfigSpec?)?.name ?: inner.simpleName), source)
        }
    }
}
