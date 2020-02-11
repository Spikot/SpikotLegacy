/*
 * Copyright 2020 Spikot project authors
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

package kr.heartpattern.spikot.command.internal

import kr.heartpattern.spikot.command.AbstractCommand
import kr.heartpattern.spikot.command.Root
import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.module.canLoad
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import kr.heartpattern.spikot.utils.catchAll
import kr.heartpattern.spikot.utils.withAccessible
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.Plugin
import java.lang.reflect.Constructor
import kotlin.reflect.KClass

@Module(priority = ModulePriority.API)
object CommandManager : AbstractModule() {
    private val commandMap: CommandMap
    private val pluginCommandConstructor: Constructor<PluginCommand>

    init {
        Bukkit.getServer().javaClass.getDeclaredField("commandMap").withAccessible { field ->
            commandMap = (field[Bukkit.getServer()] as? CommandMap)
                ?: throw IllegalStateException("Cannot extract command map")
        }

        pluginCommandConstructor = PluginCommand::class.java.getDeclaredConstructor(String::class.java, Plugin::class.java)
        pluginCommandConstructor.isAccessible = true
    }

    override fun onEnable() {
        SpikotPluginManager.forEachAnnotation<Root> { (type, plugin, _) ->
            logger.debug { "Find command: ${type.simpleName}" }

            if (!type.canLoad()) return@forEachAnnotation

            logger.catchAll("Cannot register command: ${type.simpleName}") {
                @Suppress("UNCHECKED_CAST")
                val tabExecutor = SpikotTabExecutor(CommandNode(type as KClass<out AbstractCommand>), plugin)
                val command = pluginCommandConstructor.newInstance(tabExecutor.root.handler.names.first(), plugin)
                command.executor = tabExecutor
                command.tabCompleter = tabExecutor
                command.aliases = tabExecutor.root.handler.names.toList()
                command.description = tabExecutor.root.handler.help()
                command.usage = tabExecutor.root.handler.usage()

                val helpExecutor = SpikotTabExecutor(CommandNode(type), plugin)
                val help = pluginCommandConstructor.newInstance("?"+helpExecutor.root.handler.names.first(), plugin)
                help.executor = helpExecutor
                help.tabCompleter = helpExecutor
                help.aliases = helpExecutor.root.handler.names.map{"?$it"}

                commandMap.register(plugin.name, command)
                commandMap.register(plugin.name, help)
            }
        }
    }
}