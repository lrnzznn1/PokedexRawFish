package com.lrnzznn.pokedexrawfish

class PokemonJSON {
    val name: String = ""
    val url: String = ""
}

data class PokemonListResponse(
    val results: List<PokemonJSON>
)

class PokemonDetail(
    val height: Int?,
    val weight: Int?,
    val imageUrl: String?,
    val movesList: List<Move>?
)
class Move(
    val name: String
)
