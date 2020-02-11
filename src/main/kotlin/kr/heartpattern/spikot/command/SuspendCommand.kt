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

package kr.heartpattern.spikot.command

import kr.heartpattern.spikot.command.property.SuspendCommandProperty
import org.bukkit.command.CommandSender
import java.util.*

/**
 * Command that execute with suspension.
 */
abstract class SuspendCommand : AbstractCommand() {
    internal val suspendProperties = LinkedList<SuspendCommandProperty<*>>()
    abstract suspend fun execute()

    /**
     * Suspend property that return command sender
     * @return SuspendCommandProperty return CommandSender of this command
     */
    fun suspendSender(): SuspendCommandProperty<CommandSender> {
        val property = SuspendCommandProperty(-1) { context.sender }
        suspendProperties += property
        return property
    }

    /**
     * Suspend property that return argument.
     * Position start from 0, and position does not count sub command argument.
     * For example, this abstract command's alias is sub and child of /root,
     * then position of arg1 in "/root sub arg1 arg2" is 0.
     * @param position Position of argument
     * @return SuspendCommandProperty which return argument. If argument is not presented, property return null.
     */
    fun suspendArg(position: Int): SuspendCommandProperty<String?> {
        val property = SuspendCommandProperty(position) {
            if (context.args.size > position)
                context.args[position]
            else
                null
        }
        suspendProperties += property
        return property
    }

    /**
     * Suspend property that return list of arguments.
     * Position start from 0, and position does not count sub command argument.
     * For example, this abstract command's alias is sub and child of /root,
     * then position of arg1, arg2 in "/root sub arg1 arg2" is 0, 1
     * @param range Range of position of argument
     * @return SuspendCommandProperty which return list of argument. List size can be different from size of range if argument
     * is not fully given.
     */
    fun suspendArgs(range: IntRange): SuspendCommandProperty<List<String>> {
        val property = SuspendCommandProperty(range.first) {
            when {
                context.args.size > range.last -> context.args.subList(range.first, range.last + 1)
                context.args.size > range.first -> context.args.subList(range.first, context.args.size)
                else -> emptyList()
            }
        }
        suspendProperties += property
        return property
    }

    /**
     * Suspend property that return all remaining argument after given position.
     * Position start from 0, and position does not count sub command argument.
     * For example, this abstract command's alias is sub and child of /root,
     * then position of arg1 in "/root sub arg1 arg2" is 0.
     * @param start First position of remaining argument
     * @return SuspendCommandProperty which return list of remaining argument.
     */
    fun suspendRemains(start: Int): SuspendCommandProperty<List<String>> = suspendArgs(start..Int.MAX_VALUE)
}