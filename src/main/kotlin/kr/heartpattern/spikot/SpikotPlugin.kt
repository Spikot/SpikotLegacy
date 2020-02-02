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

@file:Suppress("unused")

package kr.heartpattern.spikot

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kr.heartpattern.spikot.utils.plus
import mu.KotlinLogging
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

/**
 * Entry point of plugin which use Spikot Framework.
 * Plugin lifecycle is managed by spikot, therefore developer should not override any of method in JavaPlugin.
 * Developer should define their plugin main class like this.
 * class PluginMain: SpikotPlugin()
 */
abstract class SpikotPlugin : JavaPlugin(), CoroutineScope by MainScope() {
    val logger by lazy { KotlinLogging.logger("plugin-${name}") }

    companion object {
        fun getPlugin(name: String): Plugin {
            return Bukkit.getPluginManager().getPlugin(name)
        }

        inline fun <reified T : Plugin> getPlugin(): T {
            return Bukkit.getPluginManager().plugins.find { it is T } as T
        }
    }

    override fun onDisable() {
        cancel(CancellationException("Plugin shut down"))
    }

    open fun onCommandException(sender: CommandSender, command: Command, label: String, args: Array<String>, exception: Exception) {
        sender.sendMessage(ChatColor.RED + "커맨드 실행 중 알 수 없는 오류가 발생하였습니다")
        logger.error(exception) { "Cannot execute command" }
    }
}