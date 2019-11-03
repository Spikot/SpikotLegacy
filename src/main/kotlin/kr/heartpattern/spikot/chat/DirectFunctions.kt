@file:Suppress("unused")

package kr.heartpattern.spikot.chat

import org.bukkit.ChatColor

fun bold(builder: Builder): ChatBuilder {
    return chat { bold(builder) }
}

fun bold(text: String): ChatBuilder {
    return chat { bold(text) }
}

fun italic(builder: Builder): ChatBuilder {
    return chat { italic(builder) }
}

fun italic(text: String): ChatBuilder {
    return chat { italic(text) }
}

fun underline(builder: Builder): ChatBuilder {
    return chat { underline(builder) }
}

fun underline(text: String): ChatBuilder {
    return chat { underline(text) }
}

fun strike(builder: Builder): ChatBuilder {
    return chat { strike(builder) }
}

fun strike(text: String): ChatBuilder {
    return chat { strike(text) }
}

fun obfuscate(builder: Builder): ChatBuilder {
    return chat { obfuscate(builder) }
}

fun obfuscate(text: String): ChatBuilder {
    return chat { obfuscate(text) }
}

fun color(color: ChatColor, builder: Builder): ChatBuilder {
    return chat { color(color, builder) }
}

fun color(color: ChatColor, text: String): ChatBuilder {
    return chat { color(color, text) }
}

fun insertion(insertion: String, builder: Builder): ChatBuilder {
    return chat { insertion(insertion, builder) }
}

fun insertion(insertion: String, text: String): ChatBuilder {
    return chat { insertion(insertion, text) }
}

fun click(type: ClickEventType, clickText: String, builder: Builder): ChatBuilder {
    return chat { click(type, clickText, builder) }
}

fun click(type: ClickEventType, clickText: String, text: String): ChatBuilder {
    return chat { click(type, clickText, text) }
}

fun hover(type: HoverEventType, hoverText: ChatBuilder, builder: Builder): ChatBuilder {
    return chat { hover(type, hoverText, builder) }
}

fun hover(type: HoverEventType, hoverText: ChatBuilder, text: String): ChatBuilder {
    return chat { hover(type, hoverText, text) }
}

fun keybind(builder: Builder): ChatBuilder {
    return chat { keybind(builder) }
}

fun keybind(text: String): ChatBuilder {
    return chat { keybind(text) }
}