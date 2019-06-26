package io.github.ReadyMadeProgrammer.Spikot.nbt

fun Byte.toNBT() = WrapperNBTTagByte(this)
fun Long.toNBT() = WrapperNBTTagLong(this)
fun Int.toNBT() = WrapperNBTTagInt(this)
fun Short.toNBT() = WrapperNBTTagShort(this)
fun Double.toNBT() = WrapperNBTTagDouble(this)
fun Float.toNBT() = WrapperNBTTagFloat(this)
fun ByteArray.toNBT() = WrapperNBTTagByteArray(this)
fun IntArray.toNBT() = WrapperNBTTagIntArray(this)
fun LongArray.toNBT() = WrapperNBTTagLongArray(this)
fun String.toNBT() = WrapperNBTTagString(this)
