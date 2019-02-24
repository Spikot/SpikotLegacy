package io.github.ReadyMadeProgrammer.Spikot.config

import org.bukkit.configuration.file.YamlConfiguration
import kotlin.reflect.KClass

internal fun YamlConfiguration.getByType(key: String, type: KClass<*>): Any? {
    if (!this.contains(key)) {
        return null
    }
    return when (type) {
        Byte::class -> getByte(key)
        Short::class -> getInt(key)
        Int::class -> getInt(key)
        Long::class -> getLong(key)
        Float::class -> getDouble(key)
        Double::class -> getDouble(key)
        Char::class -> getString(key)[0]
        Boolean::class -> getBoolean(key)
        String::class -> getString(key)
        else -> null
    }
}

internal fun YamlConfiguration.getListByType(key: String, type: KClass<*>): MutableList<*>? {
    if (!this.contains(key)) {
        return null
    }
    return when (type) {
        Byte::class -> getByteList(key)
        Short::class -> getIntegerList(key)
        Int::class -> getShortList(key)
        Long::class -> getLongList(key)
        Float::class -> getFloatList(key)
        Double::class -> getDoubleList(key)
        Char::class -> getCharacterList(key)
        Boolean::class -> getBooleanList(key)
        String::class -> getStringList(key)
        else -> mutableListOf<Any>()
    }
}

private fun YamlConfiguration.getByte(key: String): Byte? {
    val value = get(key)
    return when (value) {
        is Byte -> value
        is String -> value.toByteOrNull()
        is Char -> value.toByte()
        is Number -> value.toByte()
        else -> null
    }
}