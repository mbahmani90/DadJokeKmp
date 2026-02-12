package org.example.project.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.project.data.local.Joke
import com.example.project.data.local.JokeQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.example.project.data.local.JokeDatabase

interface FavoriteJokeRepository {

    fun getAllJokes(): Flow<List<Joke>>

    suspend fun insertJokes(jokes: List<Joke>)

    suspend fun deleteJoke(id: Long)

    suspend fun deleteAllJokes()

}

class FavoriteJokeRepositoryImp(
    private val queries: JokeQueries,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : FavoriteJokeRepository {
    
    override fun getAllJokes(): Flow<List<Joke>> {
        return queries
            .selectAll()
            .asFlow()
            .mapToList(dispatcher)
    }

    override suspend fun insertJokes(jokes: List<Joke>) {
        withContext(dispatcher) {
            queries.transaction {
                jokes.forEach { joke ->
                    queries.insertJoke(
                        id = joke.id,
                        type = joke.type,
                        setup = joke.setup,
                        punchline = joke.punchline
                    )
                }
            }
        }
    }

    override suspend fun deleteJoke(id: Long) {
        withContext(dispatcher) {
            queries.deleteJoke(id)
        }
    }

    override suspend fun deleteAllJokes() {
        withContext(dispatcher) {
            queries.deleteAllJokes()
        }
    }
}