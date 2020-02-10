/*
 * Copyright 2020 HeartPattern
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.config

import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.module.canLoad
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import kr.heartpattern.spikot.utils.catchAll
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Module(ModulePriority.SYSTEM)
internal object ConfigManager : AbstractModule() {
    private lateinit var root: File
    override fun onEnable() {
        SpikotPluginManager.forEachAnnotation<Config> { (kclass, plugin) ->
            logger.debug("Find config: ${kclass.simpleName}")
            if (!kclass.canLoad()) return@forEachAnnotation
            root = File(plugin.dataFolder, "config")
            root.mkdirs()
            logger.catchAll("Cannot load config: ${kclass.simpleName}") {
                logger.debug("Load config: ${kclass.simpleName}")
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
        SpikotPluginManager.forEachAnnotation<Config> { (kclass, plugin) ->
            if (!kclass.canLoad()) return@forEachAnnotation
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
