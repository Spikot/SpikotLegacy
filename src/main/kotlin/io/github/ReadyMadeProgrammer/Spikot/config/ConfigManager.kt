package io.github.ReadyMadeProgrammer.Spikot.config

import io.github.ReadyMadeProgrammer.Spikot.module.*
import io.github.ReadyMadeProgrammer.Spikot.plugin.SpikotPluginManager
import io.github.ReadyMadeProgrammer.Spikot.utils.catchAll
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Module(loadOrder = LoadOrder.API - 1000)
object ConfigManager : AbstractModule() {
    private lateinit var root: File
    override fun onEnable() {
        SpikotPluginManager.forEach<Config> { plugin, kclass ->
            onDebug {
                logger.info("Find config: ${kclass.simpleName}")
            }
            if (!kclass.canLoad()) return@forEach
            root = File(plugin.dataFolder, "config")
            root.mkdirs()
            catchAll {
                onDebug {
                    logger.info("Load config: ${kclass.simpleName}")
                }
                val obj = kclass.objectInstance as? ConfigSpec
                val annotation = kclass.findAnnotation<Config>()
                when {
                    annotation == null -> logger.warn("Cannot load config: ${kclass.qualifiedName}")
                    obj == null -> logger.warn("Cannot load config: ${kclass.qualifiedName}")
                    else -> {
                        val file = File(root, "${obj.name ?: kclass.simpleName}.yml")
                        file.createNewFile()
                        load(kclass, "", YamlConfiguration.loadConfiguration(file))
                    }
                }
            }
        }
    }

    override fun onDisable() {
        SpikotPluginManager.forEach<Config> { plugin, kclass ->
            if (!kclass.canLoad()) return@forEach
            root = File(plugin.dataFolder, "config")
            root.mkdirs()
            val obj = kclass.objectInstance as? ConfigSpec
            obj?.yaml?.save(File(root, "${obj.name ?: kclass.simpleName}.yml"))
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
