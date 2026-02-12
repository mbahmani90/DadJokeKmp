package org.example.project.domain.useCase

import com.example.project.data.local.Joke
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import org.example.project.data.repository.JokeRepository
import org.example.project.data.repository.JokeResource

class JokeUseCase(
    private val jokeRepository: JokeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    operator fun invoke(type: List<String>): Flow<JokeResource<List<Joke>>> = flow {
        emit(JokeResource.Loading())

        val result = jokeRepository.fetchJokes(type)

        if (result is JokeResource.Error) {
            throw RuntimeException(result.message ?: "Unknown Error")
        }

        emit(JokeResource.Success(result.data))
    }
    .retry(30) { e ->
        delay(1000)
        true
    }
    .catch { e ->
        emit(JokeResource.Error(message = e.message))
    }
    .flowOn(dispatcher)
}