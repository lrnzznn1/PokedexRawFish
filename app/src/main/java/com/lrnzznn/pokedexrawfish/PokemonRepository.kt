package com.lrnzznn.pokedexrawfish

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

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

    fun getAllPokemonPaged(): PagingSource<Int, Pokemon> {
        return pokemonDao.getAllPokemonPagingSource()
    }

}
