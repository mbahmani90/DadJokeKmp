package org.example.project.domain.useCase

import com.example.project.data.local.Joke
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.example.project.data.repository.JokeRepository
import org.example.project.data.repository.JokeResource
import kotlin.String

class JokeUseCase(
    private val jokeRepository: JokeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(type: List<String>)
            : Flow<JokeResource<List<Joke>>> = flow {

        emit(JokeResource.Loading())

        val result = jokeRepository.fetchJokes(type)

        when(result){
            is JokeResource.Error -> {
                emit(JokeResource.Error<List<Joke>>(null , result.message))
            }
            is JokeResource.Loading -> {
                emit(JokeResource.Loading())
            }
            is JokeResource.Success -> {
                emit(JokeResource.Success(result.data))
            }
        }

    }.flowOn(dispatcher)
}
