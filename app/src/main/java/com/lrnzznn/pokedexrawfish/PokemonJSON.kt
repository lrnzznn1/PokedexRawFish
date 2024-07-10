package com.lrnzznn.pokedexrawfish

import kotlinx.serialization.Serializable

class PokemonJSON {
    val name: String = ""
    val url: String = ""
}

data class PokemonListResponse(
    val results: List<PokemonJSON>
)

@Serializable
data class PokemonDetail(
    val id: Int,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val moves: List<Move>
)

@Serializable
data class Sprites(
    val front_default: String?
)

@Serializable
data class Move(
    val move: MoveName
)

@Serializable
data class MoveName(
    val name: String
)

class BooleanWrapper(var value: Boolean)
