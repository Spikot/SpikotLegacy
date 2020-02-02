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

package kr.heartpattern.spikot.command.internal

import kr.heartpattern.spikot.command.AbstractCommand
import kr.heartpattern.spikot.command.CommandContext
import kr.heartpattern.spikot.command.internal.handler.AbstractCommandHandler
import kotlin.reflect.KClass

internal class CommandNode(
    val handler: AbstractCommandHandler
) {
    companion object {
        inline operator fun <reified T : AbstractCommand> invoke(): CommandNode {
            return CommandNode(T::class)
        }

        operator fun <T : AbstractCommand> invoke(type: KClass<T>): CommandNode {
            return CommandNode(AbstractCommandHandler.create(type))
        }
    }

    fun execute(context: CommandContext) {
        return resolve(context, AbstractCommandHandler::execute)
    }

    fun complete(context: CommandContext): List<String> {
        return resolve(context, AbstractCommandHandler::complete)
    }

    fun completeChild(context: CommandContext): List<String>{
        return resolve(context, AbstractCommandHandler::completeChild)
    }

    fun help(context: CommandContext): String {
        return resolve(context){help()}
    }

    fun usage(context: CommandContext): String {
        return resolve(context){usage()}
    }

    private fun <T> resolve(context: CommandContext, executor: AbstractCommandHandler.(CommandContext) -> T): T {
        return resolve(context, 0, executor)
    }

    private fun <T> resolve(context: CommandContext, pos: Int, executor: AbstractCommandHandler.(CommandContext) -> T): T {
        if (pos == context.args.size)
            return handler.executor(context.copy(args=context.args.subList(pos, context.args.size)))

        for (child in handler.childs)
            for (name in child.handler.names)
                if (context.args[pos].equals(name, ignoreCase = true))
                    return child.resolve(context, pos + 1, executor)

        return handler.executor(context.copy(args=context.args.subList(pos, context.args.size)))
    }
}