package io.github.ReadyMadeProgrammer.Spikot.command

import com.google.common.collect.HashMultimap
import org.bukkit.Server
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.permissions.PermissionAttachmentInfo
import org.bukkit.plugin.Plugin
import java.util.*

class CommandGraph<T : CommandHandler>(
        private val child: MutableMap<String, CommandGraph<*>>,
        internal val info: CommandInfoBuilder,
        private val constructor: (CommandContext) -> T) {
    companion object {
        operator fun <T : CommandHandler> invoke(klass: (CommandContext) -> T): CommandGraph<T> {
            val instance = klass(CommandContext(HashMultimap.create(), LinkedList(), DummyCommandSender))
            val info = instance.info
            val child = mutableMapOf<String, CommandGraph<*>>()
            info.child.childs.map { CommandGraph(it) }.forEach { k ->
                k.info.name.forEach {
                    child[it] = k
                }
            }
            return CommandGraph(child, info, klass)
        }
    }

    fun invoke(context: CommandContext) {
        if (context.argument.size != 0 && child.containsKey(context.argument[0])) {
            context.argument.removeFirst()
            child[context.argument[0]]!!.invoke(context)
        } else {
            val instance = constructor(context)
            instance.execute()
        }
    }

    fun complete(context: CommandContext): Set<String> {
        return if (context.argument.size != 0 && child.containsKey(context.argument[0])) {
            context.argument.removeFirst()
            child[context.argument[0]]!!.complete(context)
        } else {
            info.completer(context)
        }
    }
}

object DummyCommandSender : CommandSender {
    override fun sendMessage(p0: String?) {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun sendMessage(p0: Array<out String>?) {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun isPermissionSet(p0: String?): Boolean {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun isPermissionSet(p0: Permission?): Boolean {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun addAttachment(p0: Plugin?, p1: String?, p2: Boolean): PermissionAttachment {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun addAttachment(p0: Plugin?): PermissionAttachment {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun addAttachment(p0: Plugin?, p1: String?, p2: Boolean, p3: Int): PermissionAttachment {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun addAttachment(p0: Plugin?, p1: Int): PermissionAttachment {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun getName(): String {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun isOp(): Boolean {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun getEffectivePermissions(): MutableSet<PermissionAttachmentInfo> {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun getServer(): Server {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun removeAttachment(p0: PermissionAttachment?) {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun recalculatePermissions() {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun hasPermission(p0: String?): Boolean {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun hasPermission(p0: Permission?): Boolean {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }

    override fun setOp(p0: Boolean) {
        throw NotImplementedError("You should not call DummyCommandSender's method")
    }
}