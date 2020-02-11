/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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