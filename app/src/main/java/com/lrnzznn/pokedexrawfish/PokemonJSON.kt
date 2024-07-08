package com.lrnzznn.pokedexrawfish

import kotlinx.serialization.Serializable

class PokemonJSON {
    val id: Int = 0
    val name: String = ""
    val url: String = ""
}

data class PokemonListResponse(
    val results: List<PokemonJSON>
)

@Serializable
data class PokemonDetail(
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val moves: List<Move>
)

@Serializable
data class Sprites(
    val front_default: String?
    // Altre immagini o sprite se necessario
)

@Serializable
data class Move(
    val move: MoveName
)

@Serializable
data class MoveName(
    val name: String
)