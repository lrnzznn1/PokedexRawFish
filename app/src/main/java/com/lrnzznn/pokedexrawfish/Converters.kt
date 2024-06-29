package com.lrnzznn.pokedexrawfish

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromListString(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toListString(value: String): List<String> {
        return value.split(",")
    }
}
