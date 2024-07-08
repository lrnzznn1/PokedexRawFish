package com.lrnzznn.pokedexrawfish

import android.util.Log
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    var currentPage = 0
    private val pageSize = 20

    private val _pokemonListState = mutableStateOf<List<Pokemon>>(emptyList())
    val pokemonListState: State<List<Pokemon>> = _pokemonListState

    init {
        viewModelScope.launch {
            loadMorePokemons(0, pageSize)
        }
    }

    suspend fun loadMorePokemons(offset: Int, limit: Int) {
        try {
            val pokemons = repository.getPokemonInRange(offset, limit)
            _pokemonListState.value = pokemons // Sovrascrive la lista corrente
        } catch (e: Exception) {
            Log.e("PokemonViewModel", "Error loading Pokémon", e)
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
        }
    }

    fun deleteAllPokemon() {
        viewModelScope.launch {
            repository.deleteAllPokemon()
        }
    }
}
