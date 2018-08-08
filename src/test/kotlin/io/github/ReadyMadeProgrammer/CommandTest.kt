package io.github.ReadyMadeProgrammer

import io.github.ReadyMadeProgrammer.Spikot.command.CommandContext
import io.github.ReadyMadeProgrammer.Spikot.command.CommandHandler
import java.util.concurrent.LinkedBlockingQueue

val resultQueue = LinkedBlockingQueue<String>()

class SomeRootCommand(commandContext: CommandContext) : CommandHandler(commandContext) {
    val args0: Int? by arg(0).notNull("Must be Null").cast("Cannot Cast") {
        resultQueue.put("CAST")
        it.toInt()
    }.verify {
        resultQueue.put("VERI")
        it == 2
    }
    override val info = info {
        name = setOf("a", "b")
        help = "Some Help Message"
        description = "Some Description Message"

    }

    override fun execute() {
        resultQueue.put("AE")
    }

}

class CSubCommand(commandContext: CommandContext) : CommandHandler(commandContext) {

    override val info = info {
        name = setOf("c")
        help = "c Help Message"
        description = "Some C Description Message"
    }

    override fun execute() {
        resultQueue.put("CE")
    }
}

class DSubCommand(commandContext: CommandContext) : CommandHandler(commandContext) {
    override val info = info {
        name = setOf("d")
        help = "d Help Message"
        description = "Some D Description Message"
    }

    override fun execute() {
        resultQueue.put("DE")
    }
}

class CommandTest {
    fun basicTest() {

    }
}