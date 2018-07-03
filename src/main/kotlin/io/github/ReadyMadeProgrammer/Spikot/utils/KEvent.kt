package io.github.ReadyMadeProgrammer.Spikot.utils

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

inline fun <reified T:Event> Plugin.subscribe(
        priority: EventPriority=EventPriority.NORMAL,
        ignoreCancelled: Boolean=true,
        crossinline executable:(T)->Unit)
    =Bukkit.getPluginManager()
        .registerEvent(
                T::class.java,
                object:Listener{},
                priority,
                { _, event->executable(event as T)}
                ,this,
                ignoreCancelled)
fun Plugin.subscribe(listener: Listener)
    =Bukkit.getPluginManager().registerEvents(listener,this)
