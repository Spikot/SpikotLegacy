package io.github.ReadyMadeProgrammer.Spikot.persistence

import io.github.ReadyMadeProgrammer.Spikot.event.subscribe
import io.github.ReadyMadeProgrammer.Spikot.module.*
import io.github.ReadyMadeProgrammer.Spikot.persistence.datacontroller.DataController
import io.github.ReadyMadeProgrammer.Spikot.plugin.SpikotPluginManager
import io.github.ReadyMadeProgrammer.Spikot.utils.catchAll
import org.bukkit.event.Listener
import java.io.File
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

@Module(loadOrder = LoadOrder.API)
class DataManager : AbstractModule() {
    private val registered = HashSet<DataController<*, *>>()
    override fun onEnable() {
        SpikotPluginManager.forEach<Data> { plugin, kclass ->
            onDebug {
                logger.info("Find data: ${kclass.simpleName}")
            }
            if (!kclass.canLoad()) return@forEach
            val directory = File(plugin.dataFolder, "data")
            directory.mkdirs()

            catchAll {
                onDebug {
                    logger.info("Load data: ${kclass.simpleName}")
                }
                val instance = if (kclass.isSubclassOf(DataController::class)) kclass.objectInstance as? DataController<*, *>?
                else kclass.companionObjectInstance as? DataController<*, *>?
                val targetType = if (kclass.isSubclassOf(DataController::class)) kclass.findAnnotation<Data>()!!.targetClass else kclass
                if (instance == null) {
                    logger.warn("Cannot load data: ${kclass.qualifiedName}\nData class must contains DataController as companion object or DataController itself")
                } else {
                    registered.add(instance)
                    if (instance is Listener) {
                        plugin.subscribe(instance)
                    }
                    instance.initialize(directory, targetType)
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