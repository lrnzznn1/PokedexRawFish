package com.lrnzznn.pokedexrawfish

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
    val types: MutableList<String>,
    val images: MutableList<String>
)
