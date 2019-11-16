package kr.heartpattern.spikot.adapters

import kr.heartpattern.spikot.adapter.IAdapter
import kr.heartpattern.spikot.adapter.VersionAdapterResolver
import kr.heartpattern.spikot.adapter.VersionType
import kr.heartpattern.spikot.misc.MutableProperty
import kr.heartpattern.spikot.misc.Property
import kr.heartpattern.spikot.module.IModule
import kr.heartpattern.spikot.module.LoadBefore
import kr.heartpattern.spikot.module.Module
import org.bukkit.entity.Player

interface PlayerPropertyAdapter : IAdapter {
    fun <T> get(player: Player, property: Property<T>): T
    fun <T> set(player: Player, property: MutableProperty<T>, value: T?)
    fun contains(player: Player, property: Property<*>): Boolean
    fun <T> remove(player: Player, property: MutableProperty<T>): T?

    @Module @LoadBefore([IModule::class])
    object Resolver : VersionAdapterResolver<PlayerPropertyAdapter>(PlayerPropertyAdapter::class, VersionType.BUKKIT)
    companion object : PlayerPropertyAdapter by Resolver.default
}