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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch


@Composable
fun PokemonInterface(viewModel: PokemonViewModel) {

    var isFirstLaunch by remember { mutableStateOf(true) }

    if (isFirstLaunch) {
        LaunchedEffect(Unit) {
            try {
                viewModel.deleteAllPokemon()
                val data = fetchPokemons()
                if (data.isNotEmpty()) {
                    for ((contatore, pokemon) in data.withIndex()) {
                        val pokemonDetail = fetchPokemonDetails(contatore,pokemon.url)
                        if (pokemonDetail != null) {
                            // Mappa le mosse dell'API in oggetti Move
                            val moves = pokemonDetail.moves.map { Move(MoveName(it.move.name)) }

                            viewModel.addPokemon(
                                Pokemon(
                                    id = contatore,
                                    url = pokemon.url,
                                    name = pokemon.name,
                                    height = pokemonDetail.height,
                                    weight = pokemonDetail.weight,
                                    images = pokemonDetail.sprites.front_default ?: "",
                                    movesList = moves // Assegna la lista di oggetti Move
                                )
                            )
                        } else {
                            Log.e("fetchPokemonDetails", "Failed to fetch details for ${pokemon.name}")
                            // Gestisci il fallimento nel fetch dei dettagli come preferisci
                        }
                    }
                } else {
                    Log.d("HTTP5", "Nessun dato disponibile")
                }
            } catch (e: Exception) {
                Log.e("LaunchedEffect", "Error fetching pokemons", e)
            } finally {
                isFirstLaunch = false
            }
        }
    }



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
                    Log.d("Enable", viewModel.currentPage.toString())
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