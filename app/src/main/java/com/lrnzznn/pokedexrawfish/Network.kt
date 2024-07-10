package com.lrnzznn.pokedexrawfish

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.serialization.json.Json


val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun fetchPokemons(): List<PokemonJSON> {
    return try {
        val response: HttpResponse = client.get("https://pokeapi.co/api/v2/pokemon?limit=100000&offset=0")
        val jsonString = response.readText()
        Log.d("HTTP1", jsonString)

        val pokemonListResponse = Gson().fromJson<PokemonListResponse>(jsonString, object : TypeToken<PokemonListResponse>() {}.type)
        val pokemons = pokemonListResponse.results

        Log.d("HTTP2", pokemons.toString())

        pokemons
    } catch (e: Exception) {
        Log.e("fetchPokemons", "Error fetching pokemons", e)

        emptyList()
    }
}

private val json = Json {
    ignoreUnknownKeys = true // Ignora le chiavi sconosciute nel JSON
}

suspend fun fetchPokemonDetails(urldetail : String) : PokemonDetail?{
    return try {
        val response: HttpResponse = client.get(urldetail)
        val jsonString = response.readText()
        Log.d("JSON response", jsonString)

        val pokemonDetail = json.decodeFromString<PokemonDetail>(jsonString)
        Log.d("JSON", pokemonDetail.toString())
        pokemonDetail
    }catch (e: Exception){
        Log.e("aaa", "Error fetching pokemon details", e)
        null
    }
}

fun loadPokemon(viewModel: PokemonViewModel){
    viewModel.viewModelScope.launch(IO) {
        val data = fetchPokemons()
        if (data.isNotEmpty()) {
            val concurrencyLimit = 25
            val semaphore = Semaphore(concurrencyLimit)
            coroutineScope {
                data.forEach { pokemon ->
                    launch {
                        try {
                            semaphore.acquire()

                            val pokemonDetail = fetchPokemonDetails(pokemon.url)
                            if (pokemonDetail != null) {
                                val moves = pokemonDetail.moves.map { Move(MoveName(it.move.name)) }

                                val newPokemon = Pokemon(
                                    id = pokemonDetail.id,
                                    url = pokemon.url,
                                    name = pokemon.name,
                                    height = pokemonDetail.height,
                                    weight = pokemonDetail.weight,
                                    images = pokemonDetail.sprites.front_default ?: "",
                                    movesList = moves
                                )
                                Log.d("ssss", newPokemon.id.toString())
                                viewModel.addPokemon(newPokemon)
                            } else {
                                Log.e(
                                    "fetchPokemonDetails",
                                    "Failed to fetch details for ${pokemon.name}"
                                )
                            }
                        } finally {
                            semaphore.release()
                        }
                    }
                }
            }
        } else {
            Log.d("HTTP5", "Nessun dato disponibile")
        }
    }
}