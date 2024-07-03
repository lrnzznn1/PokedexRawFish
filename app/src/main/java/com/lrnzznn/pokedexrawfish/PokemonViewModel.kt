package com.lrnzznn.pokedexrawfish

import android.util.Log
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State


class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    var currentPage = 0
    private val pageSize = 40 // Numero di Pokémon per pagina

    private val _pokemonListState = mutableStateOf<List<Pokemon>>(emptyList())
    val pokemonListState: State<List<Pokemon>> = _pokemonListState

    init {
        viewModelScope.launch {
            // All'avvio, carica i primi 40 Pokémon
            loadMorePokemons(0, pageSize)
        }
    }

    // Funzione per caricare i Pokémon in base all'offset e al limite
    suspend fun loadMorePokemons(offset: Int, limit: Int) {
        try {
            val pokemons = repository.getPokemonInRange(offset, limit)
            _pokemonListState.value = pokemons
        } catch (e: Exception) {
            // Gestione dell'errore
            Log.e("PokemonViewModel", "Error loading Pokémon", e)
        }
    }


    fun loadNextPage() {
        viewModelScope.launch {
            try {
                val offset = currentPage * pageSize
                val limit = pageSize
                val data = repository.getPokemonInRange(offset, limit)
                if (data.isNotEmpty()) {
                    _pokemonListState.value = data
                    currentPage++
                }
            } catch (e: Exception) {
                // Gestione dell'errore
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
                    _pokemonListState.value = data
                }
            } catch (e: Exception) {
                // Gestione dell'errore
                Log.e("PokemonViewModel", "Error loading previous page", e)
            }
        }
    }
    private fun getAllPokemon() {
        viewModelScope.launch {
            repository.allPokemon.collect{
                _pokemonListState.value = it // Aggiorna il MutableStateFlow con i dati emessi dal repository
            }
        }
    }

    fun addPokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            repository.insertPokemon(pokemon)
            getAllPokemon() // Aggiorna la lista dopo l'inserimento
        }
    }

    fun deleteAllPokemon() {
        viewModelScope.launch {
            repository.deleteAllPokemon()
        }
    }
}
