package com.cinemapalace.domain.models

data class Showtime(
    val id: String,
    val theaterId: String,
    val movieId: Int,
    val hall: String,
    val startTime: String
)

data class CreateShowtimeRequest(
    val theaterId: String,
    val movieId: Int,
    val hall: String,
    val startTime: String
)

data class Theater(
    val id: String,
    val name: String,
    val city: String
)

data class CreateTheaterRequest(
    val name: String,
    val city: String
)