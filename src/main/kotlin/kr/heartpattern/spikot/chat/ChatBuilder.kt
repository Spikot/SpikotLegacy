@file:Suppress("unused")

package kr.heartpattern.spikot.chat

import com.github.salomonbrys.kotson.set
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.md_5.bungee.api.chat.*
import org.bukkit.ChatColor
import org.bukkit.entity.Player

internal typealias Builder = ChatBuilder.() -> Unit

/**
 * Send message with builder
 * @receiver Player to be receive message
 * @param builder Chat builder
 */
fun Player.sendMessage(builder: Builder) {
    this.spigot().sendMessage(chat(builder).toChatComponent())
}

/**
 * Send message with builder
 * @receiver Player to be receive message
 * @param builder Chat builder
 */
fun Player.sendMessage(builder: ChatBuilder) {
    this.spigot().sendMessage(builder.toChatComponent())
}

/**
 * Create ChatBuilder instance from builder
 * @param builder Builder that configure chat
 * @return ChatBuilder instance
 */
fun chat(builder: Builder): ChatBuilder {
    val base = ChatBuilder()
    base.builder()
    return base
}

/**
 * Create ChatBuilder instance from String
 * @param text String to be set for ChatBuilder
 * @return ChatBuilder instance
 */
fun chat(text: String): ChatBuilder {
    return ChatBuilder(text)
}

/**
 * Chat representation
 */
class ChatBuilder {
    private var _keybind: Boolean = false
    private var _text: String = ""
    private var _bold: Boolean = false
    private var _italic: Boolean = false
    private var _underlined: Boolean = false
    private var _strikethrough: Boolean = false
    private var _obfuscated: Boolean = false
    private var _color: ChatColor? = null
    private var _insertion: String? = null
    private var _clickEvent: Pair<ClickEventType, String>? = null
    private var _hoverEvent: Pair<HoverEventType, ChatBuilder>? = null
    private val extra: MutableList<ChatBuilder> = ArrayList()

    constructor()

    constructor(text: String) {
        _text = text
    }

    fun bold(builder: Builder) {
        _bold = true
        this.builder()
    }

    fun bold(text: String) {
        _bold = true
        this._text = text
    }

    fun italic(builder: Builder) {
        _italic = true
        this.builder()
    }

    fun italic(text: String) {
        _italic = true
        this._text = text
    }

    fun underline(builder: Builder) {
        _underlined = true
        this.builder()
    }

    fun underline(text: String) {
        _underlined = true
        this._text = text
    }

    fun strike(builder: Builder) {
        _strikethrough = true
        this.builder()
    }

    fun strike(text: String) {
        _strikethrough = true
        this._text = text
    }

    fun obfuscate(builder: Builder) {
        _obfuscated = true
        this.builder()
    }

    fun obfuscate(text: String) {
        _obfuscated = true
        this._text = text
    }

    fun color(color: ChatColor, builder: Builder) {
        _color = color
        this.builder()
    }

    fun color(color: ChatColor, text: String) {
        _color = color
        this._text = text
    }

    fun insertion(insertion: String, builder: Builder) {
        _insertion = insertion
        this.builder()
    }

    fun insertion(insertion: String, text: String) {
        _insertion = insertion
        this._text = text
    }

    fun click(type: ClickEventType, clickText: String, builder: Builder) {
        _clickEvent = Pair(type, clickText)
        this.builder()
    }

    fun click(type: ClickEventType, clickText: String, text: String) {
        _clickEvent = Pair(type, clickText)
        this._text = text
    }

    fun hover(type: HoverEventType, hoverText: ChatBuilder, builder: Builder) {
        _hoverEvent = Pair(type, hoverText)
        this.builder()
    }

    fun hover(type: HoverEventType, hoverText: ChatBuilder, text: String) {
        _hoverEvent = Pair(type, hoverText)
        this._text = text
    }

    fun keybind(builder: Builder) {
        _keybind = true
        this.builder()
    }

    fun keybind(text: String) {
        _keybind = true
        this._text = text
    }

    fun text(text: String) {
        this._text = text
    }

    operator fun plus(text: String): ChatBuilder {
        extra.add(ChatBuilder(text))
        return this
    }

    operator fun plus(builder: ChatBuilder): ChatBuilder {
        extra.add(builder)
        return this
    }

    fun toChatComponent(): BaseComponent {
        val component = if (_keybind) {
            KeybindComponent(_text)
        } else {
            TextComponent(_text)
        }
        component.isBold = _bold
        component.isItalic = _italic
        component.isUnderlined = _underlined
        component.isStrikethrough = _strikethrough
        component.isObfuscated = _obfuscated
        component.color = _color?.asBungee() ?: ChatColor.RESET.asBungee()
        component.insertion = _insertion
        val ce = _clickEvent
        if (ce != null) {
            component.clickEvent = ClickEvent(ce.first.type, ce.second)
        }
        val he = _hoverEvent
        if (he != null) {
            component.hoverEvent = HoverEvent(he.first.type, arrayOf(he.second.toChatComponent()))
        }
        extra.forEach {
            component.addExtra(it.toChatComponent())
        }
        return component
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun toJson(): JsonObject {
        val json = JsonObject()
        if (_keybind) {
            json["keybind"] = _text
        } else {
            json["text"] = _text
        }
        json["bold"] = _bold.toString()
        json["italic"] = _italic.toString()
        json["underlined"] = _underlined.toString()
        json["strikethrough"] = _strikethrough.toString()
        json["obfuscated"] = _obfuscated.toString()
        json["color"] = _color?.name ?: "reset"
        if (_insertion != null) {
            json["insertion"] = _insertion
        }
        val cE = _clickEvent
        if (cE != null) {
            val cJson = JsonObject()
            cJson["action"] = cE.first.json
            cJson["value"] = cE.second
            json["clickEvent"] = cJson
        }
        val hE = _hoverEvent
        if (hE != null) {
            val hJson = JsonObject()
            hJson["action"] = hE.first.json
            hJson["value"] = hE.second
        }
        if (extra.isNotEmpty()) {
            val array = JsonArray()
            for (builder in extra) {
                array.add(builder.toJson())
            }
            json["extra"] = array
        }
        return json
    }

    override fun toString(): String {
        return toJson().toString()
    }
}

enum class ClickEventType(val json: String, val type: ClickEvent.Action) {
    OPEN_URL("open_url", ClickEvent.Action.OPEN_URL),
    RUN_COMMAND("run_command", ClickEvent.Action.RUN_COMMAND),
    SUGGEST_COMMAND("suggest_command", ClickEvent.Action.SUGGEST_COMMAND)
}

enum class HoverEventType(val json: String, val type: HoverEvent.Action) {
    SHOW_TEXT("show_text", HoverEvent.Action.SHOW_TEXT),
    SHOW_ITEM("show_item", HoverEvent.Action.SHOW_ITEM),
    SHOW_ENTITY("show_entity", HoverEvent.Action.SHOW_ENTITY)
}