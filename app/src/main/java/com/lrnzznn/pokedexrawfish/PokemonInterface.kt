@file:Suppress("DEPRECATION")

package com.lrnzznn.pokedexrawfish

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.google.android.ads.mediationtestsuite.MediationTestSuite.launch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun PokemonInterface(viewModel: PokemonViewModel) {

    var isFirstLaunch by remember { mutableStateOf(true) }
    viewModel.deleteAllPokemon()

    if (isFirstLaunch) {
        LaunchedEffect(Unit) {
            try {
                loadPokemon(viewModel)
            } catch (e: Exception) {
                Log.e("LaunchedEffect", "Error fetching and adding pokemons", e)
            } finally {
                isFirstLaunch = false
            }
        }
        Log.d("End","End")
    }
    Log.d("aaa","2")

   PokemonApp(viewModel = viewModel)
}



@Composable
fun PokemonItem(pokemon: Pokemon, onItemClick: (Pokemon) -> Unit) {
    Log.d("PokemonStamp", pokemon.id.toString())
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(color = Color.LightGray)
            .clickable { onItemClick(pokemon) }
    ) {
        Text(text = "ID: ${pokemon.id}")
        Text(text = "Nome: ${pokemon.name}")
        AsyncImage(
            model = pokemon.images,
            contentDescription = "Immagine di ${pokemon.name}",
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
fun PokemonDetailScreen(pokemon: Pokemon) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = "ID: ${pokemon.id}")
        Text(text = "Nome: ${pokemon.name}")
        Text(text = "Altezza: ${pokemon.height * 0.1.toFloat()} m")
        Text(text = "Peso: ${pokemon.weight * 0.1.toFloat()} kg")
        AsyncImage(
            model = pokemon.images,
            contentDescription = "Immagine di ${pokemon.name}",
            modifier = Modifier.size(100.dp)
        )
        Text(text = "Mosse:")
        pokemon.movesList.forEach { move ->
            Text(text = move.move.toString())
        }
    }
}

@Composable
fun PokemonApp(viewModel: PokemonViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "pokemon_list") {
        composable("pokemon_list") {
            PokemonListScreen(viewModel) { pokemon ->
                navController.navigate("pokemon_detail/${pokemon.id}")
            }
        }
        composable(
            "pokemon_detail/{pokemonId}",
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId")
            val pokemon = viewModel.getPokemonById(pokemonId)
            pokemon?.let {
                PokemonDetailScreen(pokemon = it)
            }
        }
    }
}

@Composable
fun PokemonListScreen(viewModel: PokemonViewModel, onItemClick: (Pokemon) -> Unit) {
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
                onClick = { viewModel.loadPreviousPage() },
            ) {
                Text("<<")
            }

            Button(
                onClick = { viewModel.loadNextPage() }
            ) {
                Text(">>")
            }
        }
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                /*
                viewModel.viewModelScope.launch {
                    try {
                        loadPokemon(viewModel)
                    } catch (e: Exception) {
                        Log.e("SwipeRefresh", "Error refreshing pokemons", e)
                    }
                }*/
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(pokemonListState) { pokemon ->
                    PokemonItem(pokemon = pokemon, onItemClick = onItemClick)
                }
            }
        }
    }
}


