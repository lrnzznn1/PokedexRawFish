package com.lrnzznn.pokedexrawfish

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    var currentPage = 0
    private val pageSize = 20

    private val _pokemonListState = mutableStateOf<List<Pokemon>>(emptyList())
    val pokemonListState: State<List<Pokemon>> = _pokemonListState



    init {
        viewModelScope.launch {
            loadMorePokemons(0, pageSize)
        }
    }

    fun loadMorePokemons(offset: Int, limit: Int) {
        viewModelScope.launch {
            try {
                val pokemons = repository.getPokemonInRange(offset, limit)
                _pokemonListState.value = pokemons // Sovrascrive la lista corrente
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Error loading Pokémon", e)
            }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            try {
                currentPage++
                val offset = currentPage * pageSize
                val limit = pageSize
                val data = repository.getPokemonInRange(offset, limit)
                if (data.isNotEmpty()) {
                    _pokemonListState.value = data // Sovrascrive la lista corrente
                }
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Error loading next page", e)
            }
        }
    }

    fun loadPreviousPage() {
        viewModelScope.launch {
            try {
                if (currentPage > 0) {
                    currentPage--
                    val offset = currentPage * pageSize
                    val limit = pageSize
                    val data = repository.getPokemonInRange(offset, limit)
                    _pokemonListState.value = data // Sovrascrive la lista corrente
                }
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Error loading previous page", e)
            }
        }
    }

    fun addPokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            repository.insertPokemon(pokemon)
            refreshPokemonList() // Dopo l'inserimento, aggiorna la lista
        }
    }

    fun deleteAllPokemon() {
        viewModelScope.launch {
            repository.deleteAllPokemon()
            refreshPokemonList() // Dopo l'inserimento, aggiorna la lista
        }
    }
    private suspend fun refreshPokemonList() {
        val offset = currentPage * pageSize
        val pokemons = repository.getPokemonInRange(offset, pageSize)
        _pokemonListState.value = pokemons
    }

    fun getPokemonById(id: Int?): Pokemon? {
        return pokemonListState.value.find { it.id == id }
    }
}
