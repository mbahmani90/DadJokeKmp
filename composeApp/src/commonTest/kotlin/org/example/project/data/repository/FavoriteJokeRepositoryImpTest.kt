package org.example.project.data.repository

import app.cash.sqldelight.db.SqlDriver
import app.cash.turbine.test
import com.example.project.data.local.Joke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.example.project.data.local.JokeDatabase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FavoriteJokeRepositoryImpTest {

    private lateinit var repository: FavoriteJokeRepositoryImp
    private lateinit var database: JokeDatabase
    private val testDispatcher = StandardTestDispatcher()

    private fun createTestDriver(): SqlDriver = createTestSqlDriver()

    @BeforeTest
    fun setup() {
        val driver = createTestDriver()
        JokeDatabase.Schema.create(driver)
        database = JokeDatabase(driver)

        repository = FavoriteJokeRepositoryImp(
            queries = database.jokeQueries,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `test flow emits updated data on insertion`() = runTest(testDispatcher) {
        val joke = Joke(id = 1, type = "KMP", setup = "Cross-platform?", punchline = "Yes!")

        repository.getAllJokes().test {
            assertEquals(emptyList(), awaitItem())

            repository.insertJokes(listOf(joke))

            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("KMP", items[0].type)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test delete joke removes item from flow`() = runTest(testDispatcher) {
        val joke = Joke(id = 5, type = "test", setup = "S", punchline = "P")

        repository.insertJokes(listOf(joke))

        repository.getAllJokes().test {
            assertEquals(1, awaitItem().size) // Initial load

            repository.deleteJoke(5)

            assertEquals(0, awaitItem().size) // Emission after delete
            cancelAndIgnoreRemainingEvents()
        }
    }
}