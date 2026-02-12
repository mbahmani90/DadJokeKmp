package org.example.project.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.data.local.DatabaseDriverFactory
import org.example.project.data.local.JokeDatabase
import org.example.project.data.local.JokeDatabase.Companion.invoke
import org.example.project.data.remote.JokeClientApi
import org.example.project.data.remote.JokeClientApiImp
import org.example.project.data.repository.FavoriteJokeRepository
import org.example.project.data.repository.FavoriteJokeRepositoryImp
import org.example.project.data.repository.JokeRepository
import org.example.project.data.repository.JokeRepositoryImp
import org.example.project.domain.useCase.JokeUseCase
import org.example.project.presentation.viewModel.FavoriteJokeViewModel
import org.example.project.presentation.viewModel.JokeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    single<JokeClientApi> { JokeClientApiImp(get()) }

    singleOf(::JokeRepositoryImp).bind<JokeRepository>()

    single { JokeUseCase(get()) }

    single<FavoriteJokeRepository> {
        FavoriteJokeRepositoryImp(get())
    }

    viewModel { JokeViewModel(get()) }
    viewModel { FavoriteJokeViewModel(get()) }
}