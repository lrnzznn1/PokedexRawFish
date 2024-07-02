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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun PokemonInterface(viewModel: PokemonViewModel) {
    val scope = rememberCoroutineScope()
    var name by remember {
        mutableStateOf(TextFieldValue("")) // Stato per il nome del Pokemon
    }

    // Ottieni lo stato attuale della lista dei Pokémon dal ViewModel
    val pokemonListState by viewModel.pokemonList.collectAsState()

    // Stato per gestire il numero di Pokémon mostrati
    var numberOfPokemonsToShow by remember { mutableStateOf(20) }


    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    LaunchedEffect(Unit) {
        try {
            val data = fetchPokemons()
            if (data.isNotEmpty()) {
                viewModel.deleteAllPokemon()
                Log.d("HTTP3", data[0].name)
                // Puoi lavorare con i dati ottenuti qui
                Log.d("HTTP4", data.size.toString())
                var contatore: Int = 0
                for (pokemon in data) {
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
                    contatore++
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





    // Ottieni LazyPagingItems per gestire la lista paginata dei Pokémon
    val lazyPagingItems = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()


    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            viewModel.deleteAllPokemon()
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(lazyPagingItems) { pokemon ->
                pokemon?.let { PokemonItem(pokemon = it) }
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