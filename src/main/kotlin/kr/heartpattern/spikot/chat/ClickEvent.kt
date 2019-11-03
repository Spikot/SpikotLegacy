@file:Suppress("unused")

package kr.heartpattern.spikot.chat

import kr.heartpattern.spikot.chat.ClickEventType.*

fun openUrl(url: String, builder: Builder): ChatBuilder {
    return chat { openUrl(url, builder) }
}

fun openUrl(url: String, text: String): ChatBuilder {
    return chat { openUrl(url, text) }
}

fun ChatBuilder.openUrl(url: String, builder: Builder) {
    click(OPEN_URL, url, builder)
}

fun ChatBuilder.openUrl(url: String, text: String) {
    click(OPEN_URL, url, text)
}

fun runCommand(command: String, builder: Builder): ChatBuilder {
    return chat { runCommand(command, builder) }
}

fun runCommand(command: String, text: String): ChatBuilder {
    return chat { runCommand(command, text) }
}

fun ChatBuilder.runCommand(command: String, builder: Builder) {
    click(RUN_COMMAND, command, builder)
}

fun ChatBuilder.runCommand(command: String, text: String) {
    click(RUN_COMMAND, command, text)
}

fun suggestCommand(command: String, builder: Builder): ChatBuilder {
    return chat { suggestCommand(command, builder) }
}

fun suggestCommand(command: String, text: String): ChatBuilder {
    return chat { suggestCommand(command, text) }
}

fun ChatBuilder.suggestCommand(command: String, builder: Builder) {
    click(SUGGEST_COMMAND, command, builder)
}

fun ChatBuilder.suggestCommand(command: String, text: String) {
    click(SUGGEST_COMMAND, command, text)
}