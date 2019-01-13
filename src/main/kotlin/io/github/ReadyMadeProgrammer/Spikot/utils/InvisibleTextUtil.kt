package io.github.ReadyMadeProgrammer.Spikot.utils

@Suppress("SpellCheckingInspection")
private const val allowedTag = "abcdefghijlmnopqstuvwxyz0123456789"
private const val allowedLength = allowedTag.length
private const val special = '§'
private const val bounder = "§r"

fun String.attachInvisible(i: Int): String {
    return this + i.encryptInvisible()
}

fun Int.encryptInvisible(): String {
    var i = this
    val builder = StringBuilder()
    builder.append(bounder)
    while (i != 0) {
        builder.append(special + allowedTag[i % allowedLength].toString())
        i /= allowedLength
    }
    builder.append(bounder)
    return builder.toString()
}

fun String.decryptInvisible(): Int {
    if (this.length < 6 || !this.startsWith(bounder) && !this.endsWith(bounder)) {
        throw IllegalArgumentException("Illegal Format")
    }
    val code = this.substring(2, this.length - 2).replace(special.toString(), "").reversed()
    var i = 0
    code.forEach {
        i *= allowedLength
        i += allowedTag.indexOf(it)
    }
    return i
}

fun String.findInvisible(): Int {
    var i = this.length - 1
    if (this[i] != 'r' || this[i - 1] != '§') throw IllegalArgumentException("Cannot found invisible code")
    i -= 2
    while (!(this[i] == 'r' && this[i - 1] == '§') && i >= 1) {
        if (!allowedTag.contains(this[i]) || this[i - 1] != '§') {
            throw IllegalArgumentException("Cannot found invisible code")
        }
        i -= 2
    }
    i--
    return this.substring(i).decryptInvisible()
}

fun String.hasInvisible(): Boolean {
    try {
        this.findInvisible()
    } catch (e: Exception) {
        return false
    }
    return true
}