package org.example.project.data.repository

import com.example.project.data.local.Joke
import kotlinx.coroutines.delay
import org.example.project.data.local.JokeDatabase
import org.example.project.data.remote.JokeClientApi
import org.example.project.data.remote.JokeDto

sealed class JokeResource<T>(val data: T? = null , val message: String? = null){

    class Success<T>(data: T? = null) : JokeResource<T>(data)
    class Loading<T>() : JokeResource<T>()
    class Error<T>(data: T? = null , message: String? = null) : JokeResource<T>(data , message)

}

interface JokeRepository {

    suspend fun fetchJokes(typeList: List<String> = mutableListOf()): JokeResource<List<Joke>>

}

class JokeRepositoryImp(
    private val jokeClientApi: JokeClientApi
): JokeRepository {

    override suspend fun fetchJokes(typeList: List<String>): JokeResource<List<Joke>> {
        return try {
            var result: JokeDto

            result = jokeClientApi.getJokes(typeList)
            while (result.type !in typeList){
                delay(1000)
                result = jokeClientApi.getJokes(typeList)
            }

            JokeResource.Success(
                listOf(
                    Joke(
                        id = result.id,
                        type = result.type,
                        setup = result.setup,
                        punchline = result.punchline
                    )
                )
            )

        } catch (e: Exception) {
            JokeResource.Error(data = null, message = e.message.toString())
        }
    }

}