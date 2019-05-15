package io.github.ReadyMadeProgrammer.Spikot.persistence.gson

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.ReadyMadeProgrammer.Spikot.module.*
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
        SpikotPluginManager.spikotPlugins.asSequence().flatMap { it.serializer.asSequence() }.filter {
            onDebug {
                logger.info("Find serializer: ${it.simpleName}")
            }
            it.canLoad()
        }.forEach {
            try {
                onDebug {
                    logger.info("Register serializer: ${it.simpleName}")
                }
                val instance = it.getInstance()
                val type = (it.java.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as Class<*>
                if (it.findAnnotation<Serializer>()?.hierarchy == true) {
                    gsonBuilder.registerTypeHierarchyAdapter(type, instance)
                } else {
                    gsonBuilder.registerTypeAdapter(type, instance)
                }
            } catch (e: Exception) {
                logger.warn(e) { "Exception while register type adapter: ${it.simpleName}" }
            }
        }
        gson = gsonBuilder.create()
    }
}