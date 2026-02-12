package org.example.project.data.repository

import kotlinx.coroutines.test.runTest
import org.example.project.data.remote.FakeJokeClientApi
import org.example.project.data.remote.JokeDto
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JokeRepositoryTest {

    private lateinit var fakeApi: FakeJokeClientApi
    private lateinit var repository: JokeRepositoryImp

    @BeforeTest
    fun setup() {
        fakeApi = FakeJokeClientApi()
        repository = JokeRepositoryImp(fakeApi)
    }

    @Test
    fun `fetchJokes returns Success when API returns matching type`() = runTest {

        val expectedJoke = JokeDto(
            id =1,
            type = "programming",
            setup = "Setup",
            punchline = "Punchline")
        fakeApi.jokesToReturn = listOf(expectedJoke)

        val result = repository.fetchJokes(typeList = listOf("programming"))

        assertTrue(result is JokeResource.Success, "Expected Success but got ${result.message}")

        val jokes = result.data!!
        assertEquals(1, jokes.size)
        assertEquals("programming", jokes.first().type)
    }


}