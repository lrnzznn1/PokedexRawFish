package com.lrnzznn.pokedexrawfish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList

    init {
        getAllPokemon()
    }

    private fun getAllPokemon() {
        viewModelScope.launch {
            repository.allPokemon.collect{
                _pokemonList.value = it // Aggiorna il MutableStateFlow con i dati emessi dal repository
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
