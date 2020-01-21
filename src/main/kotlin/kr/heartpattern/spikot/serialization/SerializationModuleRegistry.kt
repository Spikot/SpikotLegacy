package kr.heartpattern.spikot.serialization

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule
import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.plugin.FindAnnotation
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import kr.heartpattern.spikot.utils.getInstance

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@FindAnnotation(impl = [SerialModule::class])
annotation class SerializationModule

val serializationModule: SerialModule
    get() = SerializationModuleRegistry.serializationModule

val jsonSerializer: Json
    get() = SerializationModuleRegistry.jsonSerializer

val yamlSerializer: Yaml
    get() = SerializationModuleRegistry.yamlSerializer

@Module(priority = ModulePriority.SYSTEM)
object SerializationModuleRegistry : AbstractModule() {
    lateinit var serializationModule: SerialModule
        private set
    lateinit var jsonSerializer: Json
        private set
    lateinit var yamlSerializer: Yaml
        private set

    override fun onEnable() {
        serializationModule = SerializersModule {
            SpikotPluginManager.forEachAnnotation<SerializationModule> { (type, _, _) ->
                include(type.getInstance() as SerialModule)
            }
        }
        jsonSerializer = Json(JsonConfiguration.Stable, serializationModule)
        yamlSerializer = Yaml(serializationModule)
    }
}