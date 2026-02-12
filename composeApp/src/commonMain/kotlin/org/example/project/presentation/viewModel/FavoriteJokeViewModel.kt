package org.example.project.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.project.data.local.Joke
import kotlinx.coroutines.Job
import org.example.project.data.repository.FavoriteJokeRepository

data class FavoriteJokeState(
    val jokes: List<Joke> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class FavoriteJokeViewModel(
    private val repository: FavoriteJokeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoriteJokeState())
    val state: StateFlow<FavoriteJokeState> = _state.asStateFlow()

    private var searchJob: Job? = null


    fun observeJokes() {
        viewModelScope.launch {
            repository.getAllJokes()
                .catch { e ->
                    _state.update { it.copy(errorMessage = e.message) }
                }
                .collect { jokes ->
                    _state.update { it.copy(jokes = jokes, errorMessage = null) }
                }
        }
    }

    fun addJokes(jokes: List<Joke>) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.insertJokes(jokes)
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun removeJoke(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteJoke(id)
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun clearAllJokes() {
        viewModelScope.launch {
            try {
                repository.deleteAllJokes()
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun searchJokes(query: String) {
        searchJob?.cancel()
        searchJob =viewModelScope.launch {
            val flow = if (query.isBlank()) {
                repository.getAllJokes()
            } else {
                repository.searchJokes(query)
            }

            flow.catch { e ->
                _state.update { it.copy(errorMessage = e.message) }
            }
                .collect { filteredJokes ->
                    _state.update { it.copy(jokes = filteredJokes, errorMessage = null) }
                }
        }
    }
}