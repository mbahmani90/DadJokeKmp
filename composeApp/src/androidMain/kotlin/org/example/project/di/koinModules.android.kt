package org.example.project.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.data.local.AndroidDatabaseDriverFactory
import org.example.project.data.local.DatabaseDriverFactory
import org.example.project.data.local.JokeDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {

    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    single<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(androidContext())
    }

    single<JokeDatabase> {
        JokeDatabase(get<DatabaseDriverFactory>().createDriver())
    }

    single { get<JokeDatabase>().jokeQueries }

}