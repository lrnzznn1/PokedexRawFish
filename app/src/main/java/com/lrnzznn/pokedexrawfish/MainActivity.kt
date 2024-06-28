package com.lrnzznn.pokedexrawfish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lrnzznn.pokedexrawfish.ui.theme.PokedexRawFishTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexRawFishTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main()
                }
            }
        }
    }
}



@Composable
fun Main(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
        ) {
            for (i in 1..15) {
                Pokemon(modifier = modifier, i = i)
            }
        }
    }
}



@Composable
fun Pokemon(modifier: Modifier, i : Int){
    Box(
        modifier = Modifier
            .padding(top = 40.dp, bottom = 40.dp)
    ){
        Column{
            BasicText(
                text = "prova $i",
                modifier = modifier
            )
            BasicText(
                text = "descrizione di $i",
                modifier = modifier
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokedexRawFishTheme {
        Main()
    }
}