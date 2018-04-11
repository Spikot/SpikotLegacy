package io.github.ReadyMadeProgrammer.Spikot.command

import io.github.ReadyMadeProgrammer.KommandFramework.Arguments
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

fun permission(message: String, vararg permission: String)
    = {arguments: Arguments, commandSender: CommandSender->
        if(permission.any{!commandSender.hasPermission(it)}){
            commandSender.sendMessage(message)
            throw PermissionException()
        }
    }
fun onlySubCommand(help: String, vararg sub: String)
    = {arguments: Arguments, commandSender: CommandSender->
        if(arguments.size==0||!sub.any{it.equals(arguments[0],true)}) {
            commandSender.sendMessage(help)
            throw OnlySubCommandAllowedException()
        }
    }
fun limit(message: String, min: Int=-1, max:Int=-1)
    = {arguments: Arguments, commandSender: CommandSender->
        if((min!=-1&&arguments.size<min)||(max!=-1&&arguments.size>max)) {
            commandSender.sendMessage(message)
            throw ArgumentCountIncorrectException()
        }
    }
fun player(message: String, vararg index: Int)
    = { arguments: Arguments, commandSender: CommandSender ->
        if(index.any{Bukkit.getPlayerExact(arguments[it])==null}){
            commandSender.sendMessage(message)
            throw WrongArgumentException()
        }
    }