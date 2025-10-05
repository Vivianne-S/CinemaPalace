package com.example.cinemapalace.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.cinemapalace.data.model.ShowtimeWithMovie
import com.example.cinemapalace.data.repository.MovieRepository
import com.example.cinemapalace.util.Result

class HomeViewModel : ViewModel() {
    private val repository = MovieRepository()
    private val _showtimesState = MutableStateFlow<Result<List<ShowtimeWithMovie>>>(Result.Loading)
    val showtimesState: StateFlow<Result<List<ShowtimeWithMovie>>> = _showtimesState.asStateFlow()

    init {
        loadShowtimes()
    }

    fun loadShowtimes() {
        viewModelScope.launch {
            _showtimesState.value = Result.Loading
            try {
                val result = repository.getAllShowtimes()
                _showtimesState.value = result
            } catch (e: Exception) {
                _showtimesState.value = Result.Error(e.message ?: "Unknown error occurred")
                e.printStackTrace()
            }
        }
    }

    fun getMovieById(movieId: String): ShowtimeWithMovie? {
        return when (val result = _showtimesState.value) {
            is Result.Success -> {
                result.data.find { it.movie.id.toString() == movieId }
            }
            else -> null
        }
    }

}