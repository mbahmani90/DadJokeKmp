package org.example.project.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.data.local.Joke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.example.project.data.repository.JokeResource
import org.example.project.domain.useCase.JokeUseCase

data class JokeState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    var jokeList: List<Joke>? = null
)

class JokeViewModel(
    private val jokeUseCase: JokeUseCase
) : ViewModel() {

    private var _jokeStateFlow = MutableStateFlow(JokeState())
    val jokeStateFlow : StateFlow<JokeState> = _jokeStateFlow.asStateFlow()

    fun fetchJoke(){

        jokeUseCase()
            .onEach { result ->
                when(result){
                    is JokeResource.Error -> {
                        _jokeStateFlow.value = _jokeStateFlow.value.copy(
                            isLoading = false , isSuccess = false ,
                            errorMessage = result.message, jokeList = null)

                    }
                    is JokeResource.Loading -> {
                        _jokeStateFlow.value = _jokeStateFlow.value.copy(
                            isLoading = true , isSuccess = false ,
                            errorMessage = null, jokeList = null)
                    }
                    is JokeResource.Success -> {
                        _jokeStateFlow.value = _jokeStateFlow.value.copy(
                            isLoading = false , isSuccess = true ,
                            errorMessage = result.message, jokeList = result.data)
                    }
                }
            }
            .launchIn(viewModelScope)

    }


}