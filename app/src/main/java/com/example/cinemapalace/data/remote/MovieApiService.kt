package com.example.cinemapalace.data.remote

import com.example.cinemapalace.data.model.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface MovieApiService {
    @GET("/tmdb/popular")
    suspend fun getPopularMovies(): MovieListResponse

    @GET("/tmdb/now_playing")
    suspend fun getNowPlayingMovies(): MovieListResponse

    @GET("/tmdb/upcoming")
    suspend fun getUpcomingMovies(): MovieListResponse

    @GET("/tmdb/search")
    suspend fun searchMovies(@Query("query") query: String): MovieListResponse

    @GET("/tmdb/movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieListResponse
}