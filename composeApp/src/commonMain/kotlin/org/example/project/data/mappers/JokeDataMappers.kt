package org.example.project.data.mappers

import com.example.project.data.local.Joke
import org.example.project.data.remote.JokeDto

fun JokeDto.toJoke() : Joke {
    return Joke(
        id = id,
        type = type,
        setup = setup,
        punchline = punchline
    )
}