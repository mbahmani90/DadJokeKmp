package org.example.project.data.remote

class FakeJokeClientApi : JokeClientApi {

    var jokesToReturn: List<JokeDto> = emptyList()
    var shouldThrowError = false
    private var index = 0

    override suspend fun getJokes(type: List<String>): JokeDto {
        if (shouldThrowError) throw Exception("Network Failure")

        val joke = jokesToReturn[index.coerceAtMost(jokesToReturn.lastIndex)]
        index++
        return joke
    }
}