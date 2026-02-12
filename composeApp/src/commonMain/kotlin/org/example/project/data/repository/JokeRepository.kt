package org.example.project.data.repository

import com.example.project.data.local.Joke
import org.example.project.data.local.JokeDatabase
import org.example.project.data.remote.JokeClientApi

sealed class JokeResource<T>(val data: T? = null , val message: String? = null){

    class Success<T>(data: T? = null) : JokeResource<T>(data)
    class Loading<T>() : JokeResource<T>()
    class Error<T>(data: T? = null , message: String? = null) : JokeResource<T>(data , message)

}

interface JokeRepository {

    suspend fun fetchJokes(): JokeResource<List<Joke>>

}

class JokeRepositoryImp(
    private val jokeClientApi: JokeClientApi
): JokeRepository {

    override suspend fun fetchJokes(): JokeResource<List<Joke>> {
        return try{
            val result = jokeClientApi.getJokes()
            JokeResource.Success(listOf(
                Joke(id = result.id ,
                    type = result.type,
                    setup = result.setup ,
                    punchline = result.punchline)
            ))
        }catch (e: Exception){
            JokeResource.Error(data = null, message = e.message.toString())
        }
    }

}