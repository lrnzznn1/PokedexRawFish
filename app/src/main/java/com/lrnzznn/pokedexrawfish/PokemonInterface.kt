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
import androidx.compose.material.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore

@Composable
fun PokemonInterface(viewModel: PokemonViewModel) {

    var isFirstLaunch by remember { mutableStateOf(true) }
    viewModel.deleteAllPokemon()

    if (isFirstLaunch) {
        LaunchedEffect(Unit) {
            try {
                viewModel.deleteAllPokemon()
                val data = fetchPokemons()
                if (data.isNotEmpty()) {
                    val concurrencyLimit = 25
                    val semaphore = Semaphore(concurrencyLimit)
                    coroutineScope {
                        data.forEach { pokemon ->
                            launch {
                                try{
                                    semaphore.acquire() // Acquisisce un permesso

                                    val pokemonDetail = fetchPokemonDetails(pokemon.url)
                                    if (pokemonDetail != null) {
                                        // Mappa le mosse dell'API in oggetti Move
                                        val moves =
                                            pokemonDetail.moves.map { Move(MoveName(it.move.name)) }

                                        // Costruisci il Pokémon con i dettagli ottenuti
                                        val newPokemon = Pokemon(
                                            url = pokemon.url,
                                            name = pokemon.name,
                                            height = pokemonDetail.height,
                                            weight = pokemonDetail.weight,
                                            images = pokemonDetail.sprites.front_default ?: "",
                                            movesList = moves
                                        )
                                        Log.d("ssss",newPokemon.toString())
                                        viewModel.addPokemon(newPokemon)
                                        //Log.d("ssss", viewModel.pokemonListState.value.toString())
                                    } else {
                                        Log.e(
                                            "fetchPokemonDetails",
                                            "Failed to fetch details for ${pokemon.name}"
                                        )
                                        // Gestisci il fallimento nel fetch dei dettagli come preferisci
                                    }
                                }
                                finally {
                                    semaphore.release() // Rilascia il permesso
                                }
                            }
                        }
                    }
                } else {
                    Log.d("HTTP5", "Nessun dato disponibile")
                }
            } catch (e: Exception) {
                Log.e("LaunchedEffect", "Error fetching and adding pokemons", e)
            } finally {
                isFirstLaunch = false
            }
        }
        Log.d("End","End")
    }
    Log.d("aaa","2")



    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    val pokemonListState by viewModel.pokemonListState


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    viewModel.loadPreviousPage()
                },
            ) {
                Text("<<")
            }

            Button(
                onClick = { viewModel.loadNextPage() }
            ) {
                Text(">>")
            }
        }
        // Composable principale
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                //viewModel.loadMorePokemons(0, 25) // Ricarica i primi 25 Pokémon
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(pokemonListState) { pokemon ->
                    PokemonItem(pokemon = pokemon)
                    Log.d("listState", pokemonListState.size.toString())
                }
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
        Text(text = "Altezza: ${pokemon.height*0.1.toFloat()} m")
        Text(text = "Peso: ${pokemon.weight*0.1.toFloat()} kg" )
        Text(text = "Immagine: ${pokemon.images}" )
        Text(text = "Mosse: ${pokemon.movesList}" )
    }
}