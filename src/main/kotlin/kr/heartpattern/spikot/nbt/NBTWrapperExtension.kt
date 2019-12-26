package kr.heartpattern.spikot.nbt

import kr.heartpattern.spikot.adapters.NBTAdapter

/**
 * Convert java type to nbt
 * @receiver java byte
 * @return NBTTagByte
 */
fun Byte.toNBT(): WrapperNBTByte = NBTAdapter.createNBTByte(this)

/**
 * Convert java type to nbt
 * @receiver java long
 * @return NBTTagmap
 */
fun Long.toNBT(): WrapperNBTLong = NBTAdapter.createNBTLong(this)

/**
 * Convert java type to nbt
 * @receiver java int
 * @return NBTTagInt
 */
fun Int.toNBT(): WrapperNBTInt = NBTAdapter.createNBTInt(this)

/**
 * Convert java type to nbt
 * @receiver java short
 * @return NBTTagShort
 */
fun Short.toNBT(): WrapperNBTShort = NBTAdapter.createNBTShort(this)

/**
 * Convert java type to nbt
 * @receiver java double
 * @return NBTTagDouble
 */
fun Double.toNBT(): WrapperNBTDouble = NBTAdapter.createNBTDouble(this)

/**
 * Convert java type to nbt
 * @receiver java float
 * @return NBTTagFloat
 */
fun Float.toNBT(): WrapperNBTFloat = NBTAdapter.createNBTFloat(this)

/**
 * Convert java type to nbt
 * @receiver java byte array
 * @return NBTTagByteArray
 */
fun ByteArray.toNBT(): WrapperNBTByteArray = NBTAdapter.createNBTByteArray(this)

/**
 * Convert java type to nbt
 * @receiver java int array
 * @return NBTTagIntArray
 */
fun IntArray.toNBT(): WrapperNBTIntArray = NBTAdapter.createNBTIntArray(this)

/**
 * Convert java type to nbt
 * @receiver java long array
 * @return NBTTagLongArray
 */
fun LongArray.toNBT(): WrapperNBTLongArray = NBTAdapter.createNBTLongArray(this)

/**
 * Convert java type to nbt
 * @receiver java string
 * @return NBTTagString
 */
fun String.toNBT(): WrapperNBTString = NBTAdapter.createNBTString(this)

/**
 * Convert java type to nbt
 * @receiver java list
 * @return NBTTagList
 */
fun <W : WrapperNBTBase<*>> List<W>.toNBT(): WrapperNBTList<W> = NBTAdapter.createNBTList(this)

/**
 * Convert java type to nbt
 * @receiver java map
 * @return NBTTagCompound
 */
fun Map<String, WrapperNBTBase<*>>.toNBT(): WrapperNBTCompound = NBTAdapter.createNBTCompound(this)

