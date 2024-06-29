package com.lrnzznn.pokedexrawfish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lrnzznn.pokedexrawfish.ui.theme.PokedexRawFishTheme




class MainActivity : ComponentActivity() {
    private lateinit var viewModel: PokemonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inizializza il database
        val database = PokemonDatabase.getDatabase(applicationContext)
        val repository = PokemonRepository(database.pokemonDao())

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[PokemonViewModel::class.java]


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
fun Main(viewModel: PokemonViewModel, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
        ) {
            PokemonInterface(viewModel = viewModel)
        }
    }
}