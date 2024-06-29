package com.lrnzznn.pokedexrawfish

import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue


@Composable
fun PokemonInterface(viewModel: PokemonViewModel) {
    var data by remember {
        mutableStateOf("Loading...") // Stato di caricamento iniziale
    }
    val scope = rememberCoroutineScope()
    var name by remember {
        mutableStateOf(TextFieldValue("")) // Stato per il nome del Pokemon
    }

    LaunchedEffect(Unit) {
        scope.launch {
            data = fetchData()
        }
    }



    val pokemonListState = viewModel.pokemonList.collectAsState(initial = emptyList())

    pokemonListState.value.forEach { pokemon ->
        Log.d("pokemon",pokemon.name)
    }

    Button(onClick={
        viewModel.addTenRandomPokemon()
    }){
        Text(text="Aggiungi")
    }
}