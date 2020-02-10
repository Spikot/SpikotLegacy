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

package kr.heartpattern.spikot.command.internal.handler

import kotlinx.coroutines.launch
import kr.heartpattern.spikot.command.AbstractCommand
import kr.heartpattern.spikot.command.CommandContext
import kr.heartpattern.spikot.command.SuspendCommand
import kr.heartpattern.spikot.command.ValidationException
import kr.heartpattern.spikot.command.dsl.Description
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance

class SuspendCommandHandler(type: KClass<out AbstractCommand>) : AbstractCommandHandler(type) {
    private val companion = type.companionObjectInstance as Description

    override fun execute(context: CommandContext) {
        context.plugin.launch {
            val instance = type.createInstance() as SuspendCommand
            instance.context = context
            try {
                for (property in instance.properties) {
                    property.initialize(context)
                }

                for (property in instance.suspendProperties) {
                    property.initialize(context)
                }
            } catch (exception: ValidationException) {
                //Ignore
            }
            instance.execute()
        }
    }
}