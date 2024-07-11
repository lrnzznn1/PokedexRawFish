package com.lrnzznn.pokedexrawfish.interfaces

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.lrnzznn.pokedexrawfish.dataBase.PokemonDatabase
import com.lrnzznn.pokedexrawfish.dataBase.PokemonRepository
import com.lrnzznn.pokedexrawfish.dataBase.PokemonViewModel
import com.lrnzznn.pokedexrawfish.utility.PokemonViewModelFactory
import com.lrnzznn.pokedexrawfish.ui.theme.PokedexRawFishTheme


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: PokemonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the database
        val database = PokemonDatabase.getDatabase(applicationContext)
        val repository = PokemonRepository(database.pokemonDao())

        // Create ViewModel using ViewModelFactory
        val viewModelFactory = PokemonViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[PokemonViewModel::class.java]

        // Set content using Jetpack Compose
        setContent {
            PokedexRawFishTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PokemonInterface(viewModel = viewModel, context = this)
                }
            }
        }
    }
}