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

package kr.heartpattern.spikot.command

import kr.heartpattern.spikot.SpikotPlugin
import org.bukkit.command.CommandSender

/**
 * Command execution context
 * @param plugin Plugin which register this command
 * @param sender CommandSender who execute this command
 * @param args Argument list of this execution
 */
data class CommandContext(
    val plugin: SpikotPlugin,
    val sender: CommandSender,
    val label: String,
    val args: List<String>
)