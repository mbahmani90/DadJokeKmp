package org.example.project.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class JokeDto(
    val id: Long,
    val punchline: String,
    val setup: String,
    val type: String
)