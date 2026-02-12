package org.example.project.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.presentation.viewModel.FavoriteJokeViewModel
import org.example.project.presentation.viewModel.JokeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun JokeRoute(){
    val jokeViewModel : JokeViewModel = koinViewModel()
    val favoriteJokeViewModel : FavoriteJokeViewModel = koinViewModel()
    val jokeState by jokeViewModel.jokeStateFlow.collectAsState()

    LaunchedEffect(Unit){
        jokeViewModel.fetchJoke()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        jokeState.jokeList?.let { jokeList ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(
                    count = jokeList.size,
                    key = { index -> jokeList[index].id }
                ) { index ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = jokeList[index].type,
                                modifier = Modifier.padding(16.dp)
                            )
                            Text(
                                text = jokeList[index].setup,
                                modifier = Modifier.padding(16.dp)
                            )
                            Text(
                                modifier = Modifier.padding(8.dp)
                                    .clickable{
                                        favoriteJokeViewModel.addJokes(listOf(jokeList[index]))
                                    },
                                text = "⭐",
                                fontSize = 20.sp)
                        }

                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(
                onClick = {
                    jokeViewModel.fetchJoke()
                },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(text = "Get New Joke")
            }

        }

    }

}