@file:Suppress("unused")

package kr.heartpattern.spikot.item

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SpawnEggMeta

class SpawnEggItemBuilder(itemStack: ItemStack) : ItemBuilder<SpawnEggItemMetaBuilder>(itemStack) {
    constructor() : this(ItemStack(Material.MONSTER_EGG))

    override fun meta(build: SpawnEggItemMetaBuilder.() -> Unit) {
        val builder = SpawnEggItemMetaBuilder(item.itemMeta as SpawnEggMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }

}

class SpawnEggItemMetaBuilder(itemMeta: SpawnEggMeta) : ItemMetaBuilder<SpawnEggMeta>(itemMeta) {
    var spawnedType: EntityType
        get() = itemMeta.spawnedType
        set(value) {
            itemMeta.spawnedType = value
        }
}