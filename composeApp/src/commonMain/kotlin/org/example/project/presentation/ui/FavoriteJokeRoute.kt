package org.example.project.presentation.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.presentation.viewModel.FavoriteJokeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoriteJokeRoute() {
    val favoriteJokeViewModel: FavoriteJokeViewModel = koinViewModel()
    val favoriteJokeState by favoriteJokeViewModel.state.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        favoriteJokeViewModel.searchJokes("")
    }

    Column {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                favoriteJokeViewModel.searchJokes(searchQuery)
                            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search jokes...") },
            trailingIcon = {

                TextButton(
                    onClick = {
                        favoriteJokeViewModel.searchJokes(searchQuery)
                    },
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text("Search")
                }
            },
            singleLine = true
        )

        LazyColumn {

            val filteredJokes = favoriteJokeState.jokes
                .filter {
                it.setup.contains(searchQuery, ignoreCase = true) ||
                        it.type.contains(searchQuery, ignoreCase = true)
            }

            items(
                count = filteredJokes.size,
                key = { index -> filteredJokes[index].id }
            ) { index ->
                val item = filteredJokes[index]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .combinedClickable(
                            onClick = { },
                            onLongClick = {
                                favoriteJokeViewModel.removeJoke(item.id)
                            }
                        ),
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
}