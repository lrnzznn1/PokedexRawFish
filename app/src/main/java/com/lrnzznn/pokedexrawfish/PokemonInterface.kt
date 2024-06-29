package com.lrnzznn.pokedexrawfish

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
@Composable
fun PokemonInterface(){
    var data by remember {
        mutableStateOf("Loading...")
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
         scope.launch {
             data = fetchData()
         }
    }
    Log.d("Ditto",data)
}

@Preview(showBackground = true)
@Composable
fun PokemonInterfacePreview(){
    PokemonInterface()
}