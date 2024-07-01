package com.lrnzznn.pokedexrawfish

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun fetchPokemons(): List<PokemonJSON> {
    try {
        val response: HttpResponse = client.get("https://pokeapi.co/api/v2/pokemon?limit=100000&offset=0")
        val jsonString = response.readText()
        Log.d("HTTP1", jsonString)

        val pokemonListResponse = Gson().fromJson<PokemonListResponse>(jsonString, object : TypeToken<PokemonListResponse>() {}.type)
        val pokemons = pokemonListResponse.results

        Log.d("HTTP2", pokemons.toString())

        return pokemons
    } catch (e: Exception) {
        Log.e("fetchPokemons", "Error fetching pokemons", e)
        // Gestisci l'errore qui, ad esempio lanciando un'eccezione o restituendo una lista vuota
        return emptyList()
    }
}

suspend fun fetchPokemonDetails(){

}