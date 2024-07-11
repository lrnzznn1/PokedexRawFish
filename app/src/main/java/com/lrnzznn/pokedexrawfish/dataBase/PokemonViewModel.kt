package com.lrnzznn.pokedexrawfish.dataBase

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Semaphore

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    // CoroutineScope for managing coroutines in ViewModel
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Current page and page size for pagination
    private var currentPage = 0
    private val pageSize = 20

    // State for holding the list of Pokemon
    private val _pokemonListState = mutableStateOf<List<Pokemon>>(emptyList())
    val pokemonListState: State<List<Pokemon>> = _pokemonListState

    // Semaphore to manage concurrent data loading operations
    val loadSemaphore = Semaphore(1)

    // Flag to indicate network connectivity
    var connection : Boolean = false


    // Initialization block to load initial data
    init {
        viewModelScope.launch {
            loadMorePokemons()
        }
    }

    // Method to load the initial set of Pokemon
    private fun loadMorePokemons() {
        viewModelScope.launch {
            try {
                val pokemons = repository.getPokemonInRange(0, pageSize)
                _pokemonListState.value = pokemons
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Error loading Pokémon", e)
            }
        }
    }

    // Method to load the next page of Pokemon
    fun loadNextPage() {
        viewModelScope.launch {
            try {
                currentPage++
                val offset = currentPage * pageSize
                val limit = pageSize
                val data = repository.getPokemonInRange(offset, limit)
                if (data.isNotEmpty()) {
                    _pokemonListState.value = data
                }
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Error loading next page", e)
            }
        }
    }

    // Method to load the previous page of Pokemon
    fun loadPreviousPage() {
        viewModelScope.launch {
            try {
                if (currentPage > 0) {
                    currentPage--
                    val offset = currentPage * pageSize
                    val limit = pageSize
                    val data = repository.getPokemonInRange(offset, limit)
                    _pokemonListState.value = data
                }
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Error loading previous page", e)
            }
        }
    }

    // Method to add a new Pokemon to the database
    fun addPokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            val existingPokemon = getPokemonById(pokemon.id)
            if (existingPokemon != null) {
                repository.updatePokemon(pokemon)
                Log.d("addPokemon","update ${pokemon.name}")
            } else {
                repository.insertPokemon(pokemon)
                Log.d("addPokemon","insert ${pokemon.name}")
            }
            refreshPokemonList() // Refresh the list after insertion/update
        }
    }

    // Method to delete all Pokemon from the database
    fun deleteAllPokemon() {
        viewModelScope.launch {
            repository.deleteAllPokemon()
            refreshPokemonList() // Refresh the list after deletion
        }
    }

    // Helper method to refresh the list of Pokemon
    private suspend fun refreshPokemonList() {
        val offset = currentPage * pageSize
        val pokemons = repository.getPokemonInRange(offset, pageSize)
        _pokemonListState.value = pokemons
    }

    // Method to get a Pokemon by its ID
    fun getPokemonById(id: Int?): Pokemon? {
        return pokemonListState.value.find { it.id == id }
    }
}
