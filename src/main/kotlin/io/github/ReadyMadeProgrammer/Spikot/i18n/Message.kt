@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.i18n

@Target(AnnotationTarget.CLASS)
annotation class Message

interface MessageKey {
    val root: String
    val default: String
    val description: String
    operator fun invoke(vararg param: Pair<String, Any>): String {
        return message(this, *param)
    }
}

internal val messageMap = HashMap<String, HashMap<String, String>>()

fun <T : MessageKey> message(key: T, vararg param: Pair<String, Any>): String {
    var msg = messageMap[key.root]!![(key as Enum<*>).name]!!
    for (p in param) {
        msg = msg.replace("{${p.first}}", p.second.toString(), ignoreCase = true)
    }
    return msg
}