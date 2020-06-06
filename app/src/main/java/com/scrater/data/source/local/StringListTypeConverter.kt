package com.scrater.data.source.local

import androidx.room.TypeConverter

class StringListTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(";;").toList()
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {
        return list.joinToString(";;")
    }
}