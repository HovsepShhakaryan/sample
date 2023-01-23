package com.vylo.common.domain.localentity

import androidx.room.TypeConverter

open class TypeConverterString {

    @TypeConverter
    fun toList(flatStringList: String): List<String> {
        return flatStringList.split(",")
    }
    @TypeConverter
    fun fromList(listOfString: List<String>): String {
        return listOfString.joinToString(",")
    }

}