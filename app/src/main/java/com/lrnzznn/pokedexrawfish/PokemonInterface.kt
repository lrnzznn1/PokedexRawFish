@file:Suppress("DEPRECATION")

package com.lrnzznn.pokedexrawfish

import android.content.Context
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale


@Composable
fun PokemonInterface(viewModel: PokemonViewModel, context: Context) {

    var isFirstLaunch by remember { mutableStateOf(true) }

    if (isFirstLaunch) {
        LaunchedEffect("load") {
            if(isInternetAvailable(context = context)){
                viewModel.connection = true
                try {
                    loadPokemon(viewModel)
                } catch (e: Exception) {
                    Log.e("LaunchedEffect", "Error fetching and adding pokemons", e)
                } finally {
                    isFirstLaunch = false
                }
            }else{
                Log.e("Connectivity", "No internet connection available")
            }
        }
    }

   PokemonApp(viewModel = viewModel)
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
        NavigationButtons(viewModel = viewModel)
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                swipeRefreshState.isRefreshing = true
                if(viewModel.connection) loadPokemon(viewModel)
                swipeRefreshState.isRefreshing = false
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

@Composable
fun NavigationButtons(viewModel: PokemonViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { viewModel.loadPreviousPage() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1E88E5)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = "<<",
                color = Color.White,
                style = MaterialTheme.typography.button.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
        }

        Button(
            onClick = { viewModel.loadNextPage() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1E88E5)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = ">>",
                color = Color.White,
                style = MaterialTheme.typography.button.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}



@Composable
fun PokemonItem(pokemon: Pokemon, onItemClick: (Pokemon) -> Unit) {
    Log.d("PokemonStamp", pokemon.id.toString())
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onItemClick(pokemon) },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .background(color = Color(0xFFE0F7FA))
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = pokemon.images,
                contentDescription = "Immagine di ${pokemon.name}",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.surface)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ID: ${pokemon.id}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun PokemonDetailScreen(pokemon: Pokemon) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = pokemon.images,
                    contentDescription = "Immagine di ${pokemon.name}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Text(
            text = pokemon.name.capitalize(),
            style = MaterialTheme.typography.h4.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EA)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(label = "ID:", info = pokemon.id.toString())
        InfoRow(label = "Altezza:", info = "${pokemon.height * 0.1} m")
        InfoRow(label = "Peso:", info = "${pokemon.weight * 0.1} kg")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mosse:",
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF03DAC5)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        pokemon.movesList.forEach { move ->
            Text(
                text = "- ${move.move.name.capitalize()}",
                style = MaterialTheme.typography.body1.copy(
                    color = Color(0xFF000000)
                ),
                modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
            )
        }
    }
}

@Composable
fun InfoRow(label: String, info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000)
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = info,
            style = MaterialTheme.typography.body1.copy(
                color = Color(0xFF000000)
            ),
            modifier = Modifier.weight(1f)
        )
    }
}
