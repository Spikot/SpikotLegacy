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

package kr.heartpattern.spikot.command.property

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.command.CommandContext
import kr.heartpattern.spikot.command.ValidationException
import org.bukkit.command.CommandSender

/**
 * Receiver of transformer
 * @param pos Position of property. -1 for command sender, others for argument.
 * @param context Command invocation context
 */
class TransformerContext(private val pos: Int, private val context: CommandContext) {
    /**
     * Plugin which register this command
     */
    val plugin: SpikotPlugin
        get() = context.plugin

    /**
     * Command sender who invoke this command
     */
    val sender: CommandSender
        get() = context.sender

    /**
     * Argument of command
     */
    val args: List<String>
        get() = context.args

    /**
     * Fail transformation and stop executing command
     */
    fun fail(): Nothing {
        throw ValidationException(pos)
    }
}