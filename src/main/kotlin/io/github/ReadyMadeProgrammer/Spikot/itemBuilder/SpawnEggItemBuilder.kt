package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.SpawnEggMeta

class SpawnEggItemBuilder: ItemBuilder(Material.MONSTER_EGG){
    private val spawnEggMeta = meta as SpawnEggMeta
    var spawnedType: EntityType
        get() = spawnEggMeta.spawnedType
        set(value){
            spawnEggMeta.spawnedType = value
        }
}