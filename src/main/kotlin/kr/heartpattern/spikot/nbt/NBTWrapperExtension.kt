/*
 * Copyright 2020 HeartPattern
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

