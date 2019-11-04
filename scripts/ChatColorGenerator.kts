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

val builder = StringBuilder()

val colors = listOf(
    "BLACK",
    "DARK_BLUE",
    "DARK_GREEN",
    "DARK_AQUA",
    "DARK_RED",
    "DARK_PURPLE",
    "GOLD", "GRAY",
    "DARK_GRAY",
    "BLUE",
    "GREEN",
    "AQUA",
    "RED",
    "LIGHT_PURPLE",
    "YELLOW",
    "WHITE",
    "MAGIC",
    "BOLD",
    "STRIKETHROUGH",
    "UNDERLINE",
    "ITALIC",
    "RESET"
)

colors.forEach { raw ->
    val mini = transform(raw)
    builder.append(
        "fun $mini(builder: Builder): ChatBuilder = chat{$mini(builder)}\n" +
            "fun $mini(text: String): ChatBuilder = chat{$mini(text)}\n" +
            "fun ChatBuilder.$mini(builder: Builder) = color($raw,builder)\n" +
            "fun ChatBuilder.$mini(text: String) = color($raw,text)\n"
    )
}
println(builder.toString())

