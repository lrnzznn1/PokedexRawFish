package com.lrnzznn.pokedexrawfish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.lrnzznn.pokedexrawfish.ui.theme.PokedexRawFishTheme



class MainActivity : ComponentActivity() {
    private lateinit var viewModel: PokemonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inizializza il database
        val database = PokemonDatabase.getDatabase(applicationContext)
        val repository = PokemonRepository(database.pokemonDao())

        val viewModelFactory = PokemonViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[PokemonViewModel::class.java]



        setContent {
            PokedexRawFishTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Main(viewModel: PokemonViewModel) {
    PokemonInterface(viewModel = viewModel)
}