package com.example.cinemapalace.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemapalace.data.model.ShowtimeWithMovie
import com.example.cinemapalace.data.model.TheaterShowtimeGroup
import com.example.cinemapalace.data.remote.RetrofitInstance
import com.example.cinemapalace.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel : ViewModel() {

    // 🎬 Vanliga showtimes
    private val _showtimes = MutableStateFlow<Result<List<ShowtimeWithMovie>>>(Result.Loading)
    val showtimes: StateFlow<Result<List<ShowtimeWithMovie>>> = _showtimes

    // 🏙️ Grupperade visningar (biograf + salong + tider)
    private val _groupedShowtimes = MutableStateFlow<Result<List<TheaterShowtimeGroup>>>(Result.Loading)
    val groupedShowtimes: StateFlow<Result<List<TheaterShowtimeGroup>>> = _groupedShowtimes

    fun loadShowtimes(movieId: Int) {
        viewModelScope.launch {
            try {
                _showtimes.value = Result.Loading
                val response = RetrofitInstance.api.getShowtimesByMovie(movieId)
                _showtimes.value = Result.Success(response)
            } catch (e: Exception) {
                _showtimes.value = Result.Error(e.localizedMessage ?: "Något gick fel")
            }
        }
    }

    fun loadGroupedShowtimes() {
        viewModelScope.launch {
            try {
                _groupedShowtimes.value = Result.Loading
                val response = RetrofitInstance.api.getGroupedShowtimes()
                _groupedShowtimes.value = Result.Success(response)
            } catch (e: Exception) {
                _groupedShowtimes.value = Result.Error(e.localizedMessage ?: "Kunde inte ladda grupperade visningar")
            }
        }
    }
}