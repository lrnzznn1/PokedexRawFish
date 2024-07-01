package com.lrnzznn.pokedexrawfish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {


    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList

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

    fun addTenRandomPokemon() {
        viewModelScope.launch {
            repeat(10) {
                val randomName = "Pokemon ${it + 1}"
                val pokemon = Pokemon(
                    id = it + 1,
                    url = "https://placeholder.url",
                    name = randomName,
                    height = 10,
                    weight = 10,
                    types = mutableListOf("a", "b"),
                    images = mutableListOf("Image1", "Image2")
                )
                repository.insertPokemon(pokemon)
                Log.d("PokemonInsert", "Inserted Pokémon $randomName")
            }
        }
    }
}
