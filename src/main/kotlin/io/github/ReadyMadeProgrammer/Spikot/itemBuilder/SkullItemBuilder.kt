package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import org.bukkit.Material
import org.bukkit.inventory.meta.SkullMeta

class SkullItemBuilder: ItemBuilder(Material.SKULL_ITEM){
    private val skullMeta = meta as SkullMeta
    var owner: String
        get() = skullMeta.owner
        set(value){
            skullMeta.owner = value
        }
}