package kr.heartpattern.spikot.nbt

import kr.heartpattern.spikot.adapters.NBTAdapter

fun Byte.toNBT(): WrapperNBTByte = NBTAdapter.createNBTByte(this)
fun Long.toNBT(): WrapperNBTLong = NBTAdapter.createNBTLong(this)
fun Int.toNBT(): WrapperNBTInt = NBTAdapter.createNBTInt(this)
fun Short.toNBT(): WrapperNBTShort = NBTAdapter.createNBTShort(this)
fun Double.toNBT(): WrapperNBTDouble = NBTAdapter.createNBTDouble(this)
fun Float.toNBT(): WrapperNBTFloat = NBTAdapter.createNBTFloat(this)
fun ByteArray.toNBT(): WrapperNBTByteArray = NBTAdapter.createNBTByteArray(this)
fun IntArray.toNBT(): WrapperNBTIntArray = NBTAdapter.createNBTIntArray(this)
fun LongArray.toNBT(): WrapperNBTLongArray = NBTAdapter.createNBTLongArray(this)
fun String.toNBT(): WrapperNBTString = NBTAdapter.createNBTString(this)
