package io.github.ReadyMadeProgrammer.Spikot.persistence.gson

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.ReadyMadeProgrammer.Spikot.module.*
import io.github.ReadyMadeProgrammer.Spikot.plugin.SpikotPluginManager
import io.github.ReadyMadeProgrammer.Spikot.utils.catchAll
import io.github.ReadyMadeProgrammer.Spikot.utils.getInstance
import java.lang.reflect.ParameterizedType
import kotlin.reflect.full.findAnnotation

val gson: Gson
    get() = GsonManager.gson

@Module(LoadOrder.API - 1000)
object GsonManager : AbstractModule() {
    internal lateinit var gson: Gson
    override fun onEnable() {
        val gsonBuilder = Converters.registerAll(GsonBuilder())
            .setPrettyPrinting()
        SpikotPluginManager.forEach<Serializer> { plugin, kclass ->
            onDebug {
                logger.info("Find serializer: ${kclass.simpleName}")
            }
            if (!kclass.canLoad()) return@forEach
            catchAll {
                onDebug {
                    logger.info("Register serializer: ${kclass.simpleName}")
                }
                val instance = kclass.getInstance()
                val type = (kclass.java.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as Class<*>
                if (kclass.findAnnotation<Serializer>()?.hierarchy == true) {
                    gsonBuilder.registerTypeHierarchyAdapter(type, instance)
                } else {
                    gsonBuilder.registerTypeAdapter(type, instance)
                }
            }
        }
        gson = gsonBuilder.create()
    }
}