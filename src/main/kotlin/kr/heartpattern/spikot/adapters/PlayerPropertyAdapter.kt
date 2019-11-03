package kr.heartpattern.spikot.adapters

import kr.heartpattern.spikot.adapter.IAdapter
import kr.heartpattern.spikot.adapter.VersionAdapterResolver
import kr.heartpattern.spikot.adapter.VersionType
import kr.heartpattern.spikot.misc.Property
import org.bukkit.entity.Player

interface PlayerPropertyAdapter : IAdapter {
    fun <T> get(player: Player, property: Property<T>): T
    fun <T> set(player: Player, property: Property<T>, value: T?)
    fun contains(player: Player, property: Property<*>): Boolean
    fun <T> remove(player: Player, property: Property<T>): T?

    object Resolver : VersionAdapterResolver<PlayerPropertyAdapter>(PlayerPropertyAdapter::class, VersionType.BUKKIT)
    companion object : PlayerPropertyAdapter by Resolver.default
}