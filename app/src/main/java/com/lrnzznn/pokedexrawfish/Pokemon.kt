package com.lrnzznn.pokedexrawfish

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
@Entity(tableName = "pokemon_table")
data class Pokemon(
    @PrimaryKey val id: Int,
    val url: String,
    val name: String,
    val height: Int,
    val weight: Int,
    val images: String,
    val movesList: movesList
)
