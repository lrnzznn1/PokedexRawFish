package com.lrnzznn.pokedexrawfish.utility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.lrnzznn.pokedexrawfish.dataBase.PokemonRepository
import com.lrnzznn.pokedexrawfish.dataBase.PokemonViewModel

// Factory class responsible for creating instances of PokemonViewModel
class PokemonViewModelFactory(private val repository: PokemonRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokemonViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
