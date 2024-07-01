package com.lrnzznn.pokedexrawfish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class PokemonViewModelFactory(private val repository: PokemonRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokemonViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
