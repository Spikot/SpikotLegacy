package io.github.ReadyMadeProgrammer.Spikot.command

import io.github.ReadyMadeProgrammer.KommandFramework.Arguments
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

fun player() = {args: Arguments, sender: CommandSender->
    val last = if(args.size==0)"" else args[args.size-1].trim()
    Bukkit.getOnlinePlayers().filter{it.name.startsWith(last)}
}

fun oneOf(vararg list: String) = {args: Arguments, sender: CommandSender->
    val last = if(args.size==0)"" else args[args.size-1].trim()
    list.filter{it.startsWith(last)}
}

