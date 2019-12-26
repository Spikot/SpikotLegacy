package kr.heartpattern.spikot.misc

import java.util.*

/**
 * Represent type converter
 * @param B Back type
 * @param F Front type
 */
interface Converter<B, F> {
    companion object {
        /**
         * Create Converter from read and write lambda
         * @param read Back to Front converter
         * @param write Front to Back converter
         * @return Converter with given convert lambda
         */
        operator fun <B, F> invoke(read: (B) -> F, write: (F) -> B): Converter<B, F> {
            return object : Converter<B, F> {
                override fun read(raw: B): F {
                    return read(raw)
                }

                override fun write(converted: F): B {
                    return write(converted)
                }
            }
        }
    }

    /**
     * Convert back type to front type
     * @param raw Back type instance to convert
     * @return Converted back type
     */
    fun read(raw: B): F

    /**
     * Convert front type to back type
     * @param converted Front type instance to convert
     * @return Converted front type
     */
    fun write(converted: F): B
}

/**
 * Converter that convert string
 * @param R Front type
 */
interface StringConverter<R> : Converter<String, R> {
    companion object {
        /**
         * Default UUID converter
         */
        val UUID = object : StringConverter<UUID> {
            override fun read(raw: String): UUID {
                return java.util.UUID.fromString(raw)
            }

            override fun write(converted: UUID): String {
                return converted.toString()
            }
        }

        /**
         * Default String converter
         */
        val STRING = object : StringConverter<String> {
            override fun read(raw: String): String {
                return raw
            }

            override fun write(converted: String): String {
                return converted
            }
        }

        /**
         * Default Int converter
         */
        val INT = object : StringConverter<Int> {
            override fun read(raw: String): Int {
                return raw.toInt()
            }

            override fun write(converted: Int): String {
                return converted.toString()
            }
        }
    }
}