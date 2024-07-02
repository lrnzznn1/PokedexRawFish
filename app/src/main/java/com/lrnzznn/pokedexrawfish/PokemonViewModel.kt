package com.lrnzznn.pokedexrawfish

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())

    init {
        getAllPokemon()
    }

    val pokemonPagingFlow: Flow<PagingData<Pokemon>> = Pager(PagingConfig(pageSize = 10)) {
        repository.getAllPokemonPaged()
    }.flow.cachedIn(viewModelScope)


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
