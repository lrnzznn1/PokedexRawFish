package com.lrnzznn.pokedexrawfish

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


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
            data = fetchPokemons()
        }
    }

    Log.d("HTTP",data)

    val pokemonListState by viewModel.pokemonList.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = false),
        onRefresh = {
            viewModel.deleteAllPokemon()
        }
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Lista dei Pokémon
            for (pokemon in pokemonListState) {
                PokemonItem(pokemon = pokemon)
            }

            // Bottone per aggiungere 10 Pokémon casuali
            Button(
                onClick = {
                    viewModel.addTenRandomPokemon()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Aggiungi 10 Pokémon")
            }
            Button(
                onClick = {
                    viewModel.deleteAllPokemon()
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Rimuovi i pokemon")
            }
        }
    }
}



@Composable
fun PokemonItem(pokemon: Pokemon) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(color = Color.LightGray)
    ) {
        Text(text = "Nome: ${pokemon.name}")
        Text(text = "Altezza: ${pokemon.height}")
        Text(text = "Peso: ${pokemon.weight}")
        Text(text = "Tipi: ${pokemon.types.joinToString(", ")}")
    }
}