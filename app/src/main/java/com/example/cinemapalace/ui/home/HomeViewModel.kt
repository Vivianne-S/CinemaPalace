package com.example.cinemapalace.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapalace.data.model.TmdbMovie
import com.example.cinemapalace.data.repository.MovieRepository
import com.example.cinemapalace.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _popular = MutableStateFlow<Result<List<TmdbMovie>>>(Result.Loading)
    val popular: StateFlow<Result<List<TmdbMovie>>> = _popular.asStateFlow()

    private val _nowPlaying = MutableStateFlow<Result<List<TmdbMovie>>>(Result.Loading)
    val nowPlaying: StateFlow<Result<List<TmdbMovie>>> = _nowPlaying.asStateFlow()

    private val _upcoming = MutableStateFlow<Result<List<TmdbMovie>>>(Result.Loading)
    val upcoming: StateFlow<Result<List<TmdbMovie>>> = _upcoming.asStateFlow()

    init {
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            _popular.value = repository.getPopularMovies().mapData { it.results }
            _nowPlaying.value = repository.getNowPlayingMovies().mapData { it.results }
            _upcoming.value = repository.getUpcomingMovies().mapData { it.results }
        }
    }

    fun getMovieById(movieId: String): TmdbMovie? {
        val allMovies = listOf(
            (_popular.value as? Result.Success)?.data ?: emptyList(),
            (_nowPlaying.value as? Result.Success)?.data ?: emptyList(),
            (_upcoming.value as? Result.Success)?.data ?: emptyList()
        ).flatten()

        return allMovies.find { it.id.toString() == movieId }
    }

    private inline fun <T, R> Result<T>.mapData(transform: (T) -> R): Result<R> {
        return when (this) {
            is Result.Success -> Result.Success(transform(data))
            is Result.Error -> Result.Error(message)
            is Result.Loading -> Result.Loading
        }
    }
}