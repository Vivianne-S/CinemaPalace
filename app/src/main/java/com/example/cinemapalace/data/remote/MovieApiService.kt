package com.example.cinemapalace.data.remote

import com.example.cinemapalace.data.model.ShowtimeWithMovie
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApiService {
    @GET("/showtimes")
    suspend fun getAllShowtimes(): List<ShowtimeWithMovie>

    @GET("/showtimes/theater/{theaterId}")
    suspend fun getShowtimesByTheater(
        @Path("theaterId") theaterId: String
    ): List<ShowtimeWithMovie>
}