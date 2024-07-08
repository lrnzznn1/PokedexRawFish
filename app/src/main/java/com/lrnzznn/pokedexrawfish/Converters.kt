package com.lrnzznn.pokedexrawfish

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromListString(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toListString(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun moveListToString(moves: List<Move>): String {
        return json.encodeToString(moves)
    }

    @TypeConverter
    fun stringToMoveList(data: String): List<Move> {
        return json.decodeFromString(data)
    }
}
