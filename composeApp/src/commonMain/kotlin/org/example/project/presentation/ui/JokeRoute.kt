package org.example.project.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    var selectedCategories by remember { mutableStateOf(listOf("general", "programming")) }
    val categories = listOf("general", "programming")

    LaunchedEffect(Unit){
        jokeViewModel.fetchJoke(selectedCategories)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(categories) { category ->
                val isSelected = selectedCategories.contains(category)
                Card(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            selectedCategories = if (isSelected) {
                                selectedCategories - category
                            } else {
                                selectedCategories + category
                            }
                        }
                        .padding(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF2196F3) else Color.LightGray
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(16.dp),
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }

        if(jokeState.errorMessage != null){
            Text(
                text = jokeState.errorMessage.toString(),
                color = Color(0xFFE91E63)
            )
        }else{
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
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
                                        text = jokeList[index].punchline,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 20.dp, bottom = 8.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = "⭐",
                                            fontSize = 20.sp,
                                            modifier = Modifier.clickable {
                                                favoriteJokeViewModel.addJokes(listOf(jokeList[index]))
                                            }
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
                if(jokeState.isLoading){
                    CircularProgressIndicator()
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
                    jokeViewModel.fetchJoke(selectedCategories)
                },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(text = "Get New Joke")
            }

        }

    }

}