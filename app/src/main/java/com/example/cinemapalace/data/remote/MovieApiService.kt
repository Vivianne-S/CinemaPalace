package com.example.cinemapalace.data.remote

import com.example.cinemapalace.data.model.MovieDto
import com.example.cinemapalace.data.model.MovieListResponse
import com.example.cinemapalace.data.model.ShowtimeWithMovie
import com.example.cinemapalace.data.model.TheaterShowtimeGroup
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    // --- TMDB via din backend (Ktor) ---

    @GET("/tmdb/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("/tmdb/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("/tmdb/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("/showtimes/movie/{movieId}")
    suspend fun getShowtimesForMovie(@Path("movieId") movieId: Int): List<ShowtimeWithMovie>

    @GET("/tmdb/search")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MovieListResponse

    // Film-detaljer via backendens TMDB-proxy
    @GET("/tmdb/movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int
    ): MovieDto

    @GET("/showtimes/grouped")
    suspend fun getGroupedShowtimes(): List<TheaterShowtimeGroup>

    // --- Showtimes/biografer via din backend ---

    // Alla visningar för en viss film (denna använder vi i MovieDetails)
    @GET("/showtimes/movie/{movieId}")
    suspend fun getShowtimesByMovie(
        @Path("movieId") movieId: Int
    ): List<ShowtimeWithMovie>
}