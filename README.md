<h1 align="center">DadJokeKmp</h1>

<p align="center">
  A Kotlin Multiplatform (Android + iOS) joke app with category filtering, offline favourites and search.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF?logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Compose%20Multiplatform-1.10-4285F4?logo=jetpackcompose&logoColor=white" />
  <img src="https://img.shields.io/badge/platforms-Android%20%7C%20iOS-3DDC84" />
  <a href="../../actions/workflows/ci.yml"><img src="../../actions/workflows/ci.yml/badge.svg" /></a>
</p>

---

## Overview

DadJokeKmp fetches random jokes from the [Official Joke API](https://official-joke-api.appspot.com/), lets the user filter them by category, and saves favourites to a local database so they're available offline.

The UI, ViewModels, business logic, networking and database layer are shared between Android and iOS with **Kotlin Multiplatform** and **Compose Multiplatform**. Only the database driver and platform DI are platform-specific.

> **Status:** Android is complete. On iOS the SQLDelight driver (`DatabaseDriverFactory`) is still a stub, so favourites are Android-only for now.

## Features

- **Random jokes**, filtered by category (`general`, `programming`)
- **Automatic retry** with delay when the fetched joke doesn't match the selected categories or the network fails
- **Favourites** saved locally with SQLDelight, observed reactively with `Flow`
- **Search** across saved favourites
- **Bottom navigation** between the joke screen and favourites
- **Loading and error states** exposed from the ViewModel as `StateFlow`

## Architecture

The app uses **MVVM** with **Clean Architecture** layers:

```
composeApp/src/commonMain/kotlin/.../
├── data/
│   ├── remote/        Ktor client (JokeClientApi) + DTOs
│   ├── local/         SQLDelight driver factory (expect/actual)
│   ├── mappers/       DTO → model mappers
│   └── repository/    JokeRepository, FavoriteJokeRepository
├── domain/
│   └── useCase/       JokeUseCase (Loading/Success/Error flow, retry, dispatcher)
├── presentation/
│   ├── viewModel/     JokeViewModel, FavoriteJokeViewModel (StateFlow)
│   └── ui/            Compose screens, bottom navigation
└── di/                Koin modules (common + platform)
```

### Platform-specific code

| Concern | Android | iOS |
|---|---|---|
| SQLDelight driver | `AndroidSqliteDriver` | `NativeSqliteDriver` *(to do)* |
| Koin platform module | Android `Context` | native |

## Tech stack

| Area | Library |
|---|---|
| Language | Kotlin Multiplatform |
| UI | Compose Multiplatform, Material 3 |
| Navigation | Navigation Compose |
| Async | Coroutines & Flow |
| Networking | Ktor 3, kotlinx.serialization |
| Local database | SQLDelight 2 (+ coroutines extensions) |
| DI | Koin 4 |
| Testing | kotlin-test, Ktor `MockEngine`, fake API, in-memory SQLDelight driver |
| CI | GitHub Actions: builds the debug APK, runs unit tests, uploads the APK as an artifact |

## Tests

Shared tests in `commonTest` cover:

- `JokeClientApi`: request and response parsing with Ktor `MockEngine`
- `JokeRepository`: category matching and error handling with a fake API
- `FavoriteJokeRepository`: insert, search and delete against a real in-memory SQLDelight database (`JdbcSqliteDriver`, Android unit tests)

```bash
./gradlew testDebugUnitTest
```

## Getting started

- **Android:** `./gradlew :composeApp:assembleDebug`, or run the `composeApp` configuration in Android Studio
- **iOS:** open `iosApp/` in Xcode and run

Requires Android Studio with the Kotlin Multiplatform plugin, Xcode (for iOS) and JDK 17+.

## Author

**Majid Bahmani** · [GitHub](https://github.com/mbahmani90)
