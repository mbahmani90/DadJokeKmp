package org.example.project.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.presentation.viewModel.FavoriteJokeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoriteJokeRoute(){

    val favoriteJokeViewModel : FavoriteJokeViewModel = koinViewModel()
    val favoriteJokeState by favoriteJokeViewModel.state.collectAsState()

    LaunchedEffect(Unit){
        favoriteJokeViewModel.observeJokes()
    }

    LazyColumn {
        items(
            count = favoriteJokeState.jokes.size ,
            key = { index ->
                favoriteJokeState.jokes[index].id
            }){ index ->

                val item = favoriteJokeState.jokes[index]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = item.type,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = item.setup,
                        modifier = Modifier.padding(16.dp)
                    )
                }

            }
    }

}