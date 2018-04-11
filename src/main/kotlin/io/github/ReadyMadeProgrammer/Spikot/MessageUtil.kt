package io.github.ReadyMadeProgrammer.Spikot

import org.bukkit.ChatColor

operator fun ChatColor.plus(message: String) = this.toString()+message
operator fun ChatColor.plus(color: ChatColor) = this.toString()+color.toString()
