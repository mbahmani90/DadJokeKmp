package org.example.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface JokeClientApi {

    suspend fun getJokes() : JokeDto

}

class JokeClientApiImp(
    private val client: HttpClient
): JokeClientApi {
    override suspend fun getJokes(): JokeDto {
        return client.get("https://official-joke-api.appspot.com/jokes/random").body()
    }

}