package com.lrnzznn.pokedexrawfish.dataBase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


// Repository class that serves as an abstraction layer for accessing Pokemon data.
class PokemonRepository(private val pokemonDao: PokemonDao) {

    suspend fun insertPokemon(pokemon: Pokemon) {
        pokemonDao.insertPokemon(pokemon)
    }
    suspend fun updatePokemon(pokemon: Pokemon) {
        pokemonDao.updatePokemon(pokemon)
    }

    suspend fun deleteAllPokemon(){
        return pokemonDao.deleteAllPokemon()
    }

    suspend fun getPokemonInRange(offset: Int, limit: Int): List<Pokemon> {
        return withContext(Dispatchers.IO) {
            pokemonDao.getPokemonInRange(offset, limit)
        }
    }
}
