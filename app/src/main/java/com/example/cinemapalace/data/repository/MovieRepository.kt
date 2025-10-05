// app/src/main/java/com/example/cinemapalace/data/repository/MovieRepository.kt
package com.example.cinemapalace.data.repository

import com.example.cinemapalace.data.model.ShowtimeWithMovie
import com.example.cinemapalace.data.remote.MovieApiService
import com.example.cinemapalace.data.remote.RetrofitInstance
import com.example.cinemapalace.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository {
    private val api: MovieApiService = RetrofitInstance.api

    suspend fun getAllShowtimes(): Result<List<ShowtimeWithMovie>> = withContext(Dispatchers.IO) {
        try {
            val showtimes = api.getAllShowtimes()
            Result.Success(showtimes)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error("Kunde inte hämta filmer: ${e.message ?: "Okänt fel"}")
        }
    }
}