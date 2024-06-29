package com.lrnzznn.pokedexrawfish

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.*

val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun fetchData(): String {
    val response: HttpResponse = client.get("https://pokeapi.co/api/v2/pokemon?limit=100000&offset=0")
    return response.readText()
}