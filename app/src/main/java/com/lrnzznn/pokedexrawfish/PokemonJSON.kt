package com.lrnzznn.pokedexrawfish

class PokemonJSON {
    val name: String = ""
    val url: String = ""
}

data class PokemonListResponse(
    val results: List<PokemonJSON>
)