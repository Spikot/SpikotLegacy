package io.github.ReadyMadeProgrammer.Spikot.chat

import org.bukkit.ChatColor

fun main(args: Array<String>) {
    val builder = StringBuilder()

    ChatColor.values().map { it.name }.forEach { raw ->
        val mini = transform(raw)
        builder.append("fun $mini(builder: Builder): ChatBuilder{\n" +
                "    return chat{$mini(builder)}\n" +
                "}\n" +
                "\n" +
                "fun $mini(text: String): ChatBuilder{\n" +
                "    return chat{$mini(text)}\n" +
                "}\n" +
                "\n" +
                "fun ChatBuilder.$mini(builder: Builder){\n" +
                "    color($raw,builder)\n" +
                "}\n" +
                "\n" +
                "fun ChatBuilder.$mini(text: String){\n" +
                "    color($raw,text)\n" +
                "}\n\n")
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