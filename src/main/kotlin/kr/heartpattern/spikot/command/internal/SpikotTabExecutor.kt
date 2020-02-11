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

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.command.CommandContext
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

internal class SpikotTabExecutor(val root: CommandNode, val plugin: SpikotPlugin) : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command?, label: String, args: Array<out String>): Boolean {
        root.execute(CommandContext(plugin, sender, label, args.toList()))
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return root.complete(CommandContext(plugin, sender, alias, args.toList())).toMutableList()
    }
}