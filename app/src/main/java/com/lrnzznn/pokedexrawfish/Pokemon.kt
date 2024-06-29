package com.lrnzznn.pokedexrawfish

import androidx.compose.ui.text.font.FontWeight
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "pokemon_table")
data class Pokemon(
    @PrimaryKey val id: Int,
    val url: String,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: MutableList<Int>,
    val images: MutableList<String>
)
