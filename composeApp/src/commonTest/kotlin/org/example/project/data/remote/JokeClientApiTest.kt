package org.example.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class JokeClientApiTest {

    private val mockEngine = MockEngine { request ->
        assertEquals(
            "https://official-joke-api.appspot.com/jokes/random",
            request.url.toString()
        )

        respond(
            content = """
                {
                  "id": 123,
                  "type": "general",
                  "setup": "Why do programmers prefer dark mode?",
                  "punchline": "Because light attracts bugs."
                }
            """.trimIndent(),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }

    private val httpClient = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    private val api = JokeClientApiImp(httpClient)

    @Test
    fun `resquest random joke and get response JokeDto`() = runTest {
        val result = api.getJokes(listOf("general"))

        assertNotNull(result)
        assertEquals(123, result.id)
        assertEquals("general", result.type)
        assertEquals(
            "Why do programmers prefer dark mode?",
            result.setup
        )
    }

}