package com.lrnzznn.pokedexrawfish

data class PokemonDetail(
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val imageUrl: String // Supponiamo che ci sia un campo per l'URL dell'immagine del Pokémon
)
