package io.github.ReadyMadeProgrammer.Spikot.persistence

import io.github.ReadyMadeProgrammer.Spikot.event.subscribe
import io.github.ReadyMadeProgrammer.Spikot.module.*
import io.github.ReadyMadeProgrammer.Spikot.persistence.datacontroller.DataController
import io.github.ReadyMadeProgrammer.Spikot.utils.catchAll
import org.bukkit.event.Listener
import java.io.File
import kotlin.reflect.full.companionObjectInstance

@Module(loadOrder = LoadOrder.API)
class DataManager : AbstractModule() {
    private val registered = HashSet<DataController<*, *>>()
    override fun onEnable() {
        SpikotPluginManager.spikotPlugins.forEach { plugin ->
            val directory = File(plugin.plugin.dataFolder, "data")
            directory.mkdirs()
            plugin.data.filter {
                onDebug {
                    logger.info("Find data: ${it.simpleName}")
                }
                it.canLoad()
            }.forEach { type ->
                catchAll {
                    onDebug {
                        logger.info("Load data: ${type.simpleName}")
                    }
                    val companion = type.companionObjectInstance as? DataController<*, *>?
                    if (companion == null) {
                        logger.warn("Cannot load data: ${type.qualifiedName}\nData class must contains DataController as companion object")
                    } else {
                        registered.add(companion)
                        if (companion is Listener) {
                            plugin.plugin.subscribe(companion)
                        }
                        companion.initialize(directory, type)
                    }
                }
            }
        }
    }

    override fun onDisable() {
        registered.forEach { controller ->
            controller.destroy()
        }
    }
}