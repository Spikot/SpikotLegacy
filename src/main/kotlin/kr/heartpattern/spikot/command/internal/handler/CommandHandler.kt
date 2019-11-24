package kr.heartpattern.spikot.command.internal.handler

import kr.heartpattern.spikot.command.*
import kr.heartpattern.spikot.command.ValidationException
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class CommandHandler(type: KClass<out AbstractCommand>) : AbstractCommandHandler(type) {
    override fun execute(context: CommandContext) {
        val instance = type.createInstance() as Command
        instance.context = context
        try {
            for (property in instance.properties) {
                property.initialize(context)
            }
        } catch (exception: ValidationException) {
            //Ignore
        }
        instance.execute()
    }
}