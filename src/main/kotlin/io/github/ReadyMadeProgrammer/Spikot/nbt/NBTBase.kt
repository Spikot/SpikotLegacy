@file:Suppress("SpellCheckingInspection")

//package io.github.ReadyMadeProgrammer.Spikot.nbt
//
//import io.github.ReadyMadeProgrammer.Spikot.reflections.ReflectionUtils
//
//enum class TagType(val id: Byte, private val shortType: String){
//    END(0,"End"),
//    BYTE(1,"Byte"),
//    SHORT(2,"Short"),
//    INT(3,"Int"),
//    LONG(4,"Long"),
//    FLOAT(5,"Float"),
//    DOUBLE(6,"Double"),
//    BYTE_ARRAY(7,"Byte_Array"),
//    STRING(8,"String"),
//    LIST(9,"List"),
//    COMPOUND(10,"Compound"),
//    INT_ARRAY(11,"Int_Array"),
//    LONG_ARRAY(12,"Long_Array");
//    companion object{
//        val types = listOf(
//                END,
//                BYTE,
//                SHORT,
//                INT,
//                LONG,
//                FLOAT,
//                DOUBLE,
//                BYTE_ARRAY,
//                STRING,
//                LIST,
//                COMPOUND,
//                INT_ARRAY,
//                LONG_ARRAY
//        )
//        fun fromId(val
//        )
//    }
//    val type = "TAG_$shortType"
//}
//
//interface NBTBase: Cloneable{
//    companion object {
//        fun createTag(typeId: Byte): NBTBase {
//            TODO()
//        }
//
//        fun typeToString(typeId: Byte): String {
//            val id = typeId.toInt()
//            return when {
//                id <= 12 -> tagName[id]
//                id == 99 -> "Any Numeric Tag"
//                else -> "UNKNOWN"
//            }
//        }
//
//        private val classNBTBase = ReflectionUtils.getNmsClass("NBTBase")
//        private val methodGetTypeId = classNBTBase.getDeclaredMethod("getTypeId")
//    }
//    val handle: Any
//    val typeId: Byte
//        get() = methodGetTypeId.invoke(handle) as Byte
//}