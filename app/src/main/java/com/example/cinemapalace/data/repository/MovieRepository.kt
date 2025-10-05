package com.example.cinemapalace.data.repository

import com.example.cinemapalace.data.model.MovieListResponse
import com.example.cinemapalace.data.remote.MovieApiService
import com.example.cinemapalace.data.remote.RetrofitInstance
import com.example.cinemapalace.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository {
    private val api: MovieApiService = RetrofitInstance.api

    suspend fun getPopularMovies(): Result<MovieListResponse> = withContext(Dispatchers.IO) {
        try {
            val result = api.getPopularMovies()
            Result.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error("Kunde inte hämta topplistan: ${e.message}")
        }
    }

    suspend fun getNowPlayingMovies(): Result<MovieListResponse> = withContext(Dispatchers.IO) {
        try {
            val result = api.getNowPlayingMovies()
            Result.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error("Kunde inte hämta filmer som går nu: ${e.message}")
        }
    }

    suspend fun getUpcomingMovies(): Result<MovieListResponse> = withContext(Dispatchers.IO) {
        try {
            val result = api.getUpcomingMovies()
            Result.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error("Kunde inte hämta kommande filmer: ${e.message}")
        }
    }
}