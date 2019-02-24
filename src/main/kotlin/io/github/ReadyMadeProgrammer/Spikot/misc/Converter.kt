package io.github.ReadyMadeProgrammer.Spikot.misc

import java.util.*

interface Converter<T, R> {
    companion object{
        operator fun <T, R> invoke(read: (R)->T, write: (T)->R): Converter<T, R>{
            return object: Converter<T, R>{
                override fun read(raw: R): T {
                    return read(raw)
                }

                override fun write(converted: T): R {
                    return write(converted)
                }
            }
        }
    }
    fun read(raw: R): T
    fun write(converted: T): R
}

interface StringConverter<R> : Converter<R, String> {
    companion object {
        val UUID = object : StringConverter<UUID> {
            override fun write(converted: UUID): String {
                return converted.toString()
            }

            override fun read(raw: String): UUID {
                return java.util.UUID.fromString(raw)
            }
        }

        val STRING = object : StringConverter<String> {
            override fun write(converted: String): String {
                return converted
            }

            override fun read(raw: String): String {
                return raw
            }
        }

        val INT = object : StringConverter<Int> {
            override fun write(converted: Int): String {
                return converted.toString()
            }

            override fun read(raw: String): Int {
                return raw.toInt()
            }
        }
    }
}