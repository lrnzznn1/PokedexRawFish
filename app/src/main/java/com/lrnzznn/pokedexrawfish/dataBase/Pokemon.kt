package com.lrnzznn.pokedexrawfish.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lrnzznn.pokedexrawfish.utility.Move
import kotlinx.serialization.Serializable


// Data class representing a Pokemon entity stored in the database.
@Suppress("PLUGIN_IS_NOT_ENABLED") //Suppress IDE warning about plugin not enabled for Serializable
@Serializable
@Entity(tableName = "pokemon_table")
data class Pokemon(
    @PrimaryKey val id: Int = 0,
    val url: String,
    val name: String,
    val height: Int,
    val weight: Int,
    val images: String,
    val movesList: List<Move>
)
