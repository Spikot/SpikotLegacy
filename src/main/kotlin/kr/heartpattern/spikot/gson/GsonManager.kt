package kr.heartpattern.spikot.gson

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.module.*
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import kr.heartpattern.spikot.utils.catchAll
import kr.heartpattern.spikot.utils.getInstance
import java.lang.reflect.ParameterizedType
import kotlin.reflect.full.findAnnotation

val gson: Gson
    get() = GsonManager.gson

@Module(LoadOrder.API - 1000)
object GsonManager : AbstractModule() {
    internal lateinit var gson: Gson
    override fun onLoad(context: MutablePropertyMap) {
        super.onLoad(context)
        val gsonBuilder = Converters.registerAll(GsonBuilder())
            .setPrettyPrinting()
        SpikotPluginManager.forEachAnnotation<Serializer> { (kclass, _) ->
            onDebug {
                logger.info("Find serializer: ${kclass.simpleName}")
            }
            if (!kclass.canLoad()) return@forEachAnnotation
            logger.catchAll("Cannot create gson serializer: ${kclass.simpleName}") {
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