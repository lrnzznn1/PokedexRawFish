package com.lrnzznn.pokedexrawfish.utility

import kotlinx.serialization.Serializable

// Base class to represent a Pokémon in the list
class PokemonJSON {
    val name: String = ""
    val url: String = ""
}

// Data class representing the response of Pokémon list from the API
data class PokemonListResponse(
    val results: List<PokemonJSON>
)

// Data class representing details of a single Pokémon
@Serializable
data class PokemonDetail(
    val id: Int,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val moves: List<Move>
)

// Data class representing Pokémon sprites
@Serializable
data class Sprites(
    val frontDefault: String? // URL of the Pokémon's default front image, nullable
)

// Data class representing a Pokémon move
@Serializable
data class Move(
    val move: MoveName
)

// Data class representing the name of a Pokémon move
@Serializable
data class MoveName(
    val name: String
)