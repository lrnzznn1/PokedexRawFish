package com.lrnzznn.pokedexrawfish

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PokemonRepository(private val pokemonDao: PokemonDao) {

    val allPokemon: Flow<List<Pokemon>> = pokemonDao.getAllPokemon()

    suspend fun insertPokemon(pokemon: Pokemon) {
        pokemonDao.insertPokemon(pokemon)
    }
    suspend fun updatePokemon(pokemon: Pokemon) {
        pokemonDao.updatePokemon(pokemon)
    }
    fun getAllPokemon(){
        pokemonDao.getAllPokemon()
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
