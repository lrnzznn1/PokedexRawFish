@file:Suppress("DEPRECATION")

package com.lrnzznn.pokedexrawfish

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun PokemonInterface(viewModel: PokemonViewModel) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    LaunchedEffect(Unit) {
        try {
            val data = fetchPokemons()
            if (data.isNotEmpty()) {
                viewModel.deleteAllPokemon()
                Log.d("HTTP3", data[0].name)
                // Puoi lavorare con i dati ottenuti qui
                Log.d("HTTP4", data.size.toString())
                for ((contatore, pokemon) in data.withIndex()) {
                    viewModel.addPokemon(
                        Pokemon(
                            id = contatore,
                            url = pokemon.url,
                            name = pokemon.name,
                            height = 10,
                            weight = 10,
                            types = mutableListOf("a", "b"),
                            images = mutableListOf("a", "b")
                        )
                    )
                }
                Log.d("HTTP5", "Fino di Aggiungere")
            } else {
                // Gestisci il caso in cui non ci siano dati
            }
        } catch (e: Exception) {
            // Gestisci l'errore in modo appropriato
            Log.e("LaunchedEffect", "Error fetching pokemons", e)
        }
    }

    val pokemonList by viewModel.pokemonList.observeAsState(emptyList())

    Log.d("Test1" , "!!!!")

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            viewModel.deleteAllPokemon()
        }
    ) {
        Log.d("Test2" , "!!!!")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(pokemonList) { pokemon ->
                PokemonItem(pokemon = pokemon)
            }
        }
    }
}



@Composable
fun PokemonItem(pokemon: Pokemon) {
    Log.d("PokemonStamp",pokemon.id.toString())
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(color = Color.LightGray)
    ) {
        Text(text = "ID:  ${pokemon.id}")
        Text(text = "Nome: ${pokemon.name}")
        Text(text = "Altezza: ${pokemon.height}")
        Text(text = "Peso: ${pokemon.weight}")
        Text(text = "Tipi: ${pokemon.types.joinToString(", ")}")
    }
}