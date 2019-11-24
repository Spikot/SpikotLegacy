package kr.heartpattern.spikot.command.internal.handler

import kotlinx.coroutines.launch
import kr.heartpattern.spikot.command.*
import kr.heartpattern.spikot.command.ValidationException
import kr.heartpattern.spikot.command.dsl.Description
import kr.heartpattern.spikot.command.internal.CommandNode
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance

class SuspendCommandHandler(type: KClass<out AbstractCommand>) : AbstractCommandHandler(type) {
    private val companion = type.companionObjectInstance as Description
    override val childs: Collection<CommandNode> = companion.childs

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