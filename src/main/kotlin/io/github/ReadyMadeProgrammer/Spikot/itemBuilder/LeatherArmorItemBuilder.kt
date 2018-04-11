package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.meta.LeatherArmorMeta

class LeatherArmorItemBuilder(material: Material): ItemBuilder(material){
    private val colorMeta = item.itemMeta as LeatherArmorMeta
    var color: Color
        get()= colorMeta.color
        set(value){
            colorMeta.color = value
        }
}