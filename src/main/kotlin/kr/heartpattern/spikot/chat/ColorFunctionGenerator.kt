package kr.heartpattern.spikot.chat

import org.bukkit.ChatColor

fun main(args: Array<String>) {
    val builder = StringBuilder()

    ChatColor.values().map { it.name }.forEach { raw ->
        val mini = transform(raw)
        builder.append(
            "fun $mini(builder: Builder): ChatBuilder = chat{$mini(builder)}\n" +
                "fun $mini(text: String): ChatBuilder = chat{$mini(text)}\n" +
                "fun ChatBuilder.$mini(builder: Builder) = color($raw,builder)\n" +
                "fun ChatBuilder.$mini(text: String) = color($raw,text)\n"
        )
    }
    println(builder.toString())
}

fun transform(text: String): String {
    val lower = text.toLowerCase()
    var upper = false
    val builder = StringBuilder()
    lower.forEach {
        if (it == '_') {
            upper = true
        } else {
            if (upper) {
                upper = false
                builder.append(it.toUpperCase())
            } else {
                builder.append(it.toLowerCase())
            }
        }
    }
    return builder.toString()
}