package com.example.cinemapalace.data.model

data class TheaterShowtimeGroup(
    val theaterName: String,
    val showtimes: List<ShowtimeUi>
)

data class ShowtimeUi(
    val hallName: String,
    val totalSeats: Int,
    val availableSeats: Int,
    val time: String
)