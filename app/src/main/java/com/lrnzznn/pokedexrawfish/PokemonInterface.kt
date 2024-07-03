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
import androidx.compose.material.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData




@Composable
fun PokemonInterface(viewModel: PokemonViewModel) {

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

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    // State per il PokemonList da mostrare
    val pokemonListState by viewModel.pokemonListState.observeAsState(emptyList())


    // Effetto di lancio per inizializzare i dati all'avvio
    LaunchedEffect(Unit) {
        viewModel.loadMorePokemons(0, 40)
    }

    // Composable principale
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            //viewModel.loadMorePokemons(0, 40) // Ricarica i primi 40 Pokémon
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(pokemonListState) { pokemon ->
                PokemonItem(pokemon = pokemon)
            }
        }
    }

    // Pulsanti per la paginazione
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { viewModel.loadPreviousPage() },
            enabled = viewModel.currentPage > 0
        ) {
            Text("Pagina precedente")
        }

        Button(
            onClick = { viewModel.loadNextPage() }
        ) {
            Text("Pagina successiva")
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