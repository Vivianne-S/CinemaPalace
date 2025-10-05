package com.example.cinemapalace.data.model

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?
)

data class ShowtimeWithMovie(
    val id: String,
    val theaterId: String,
    val hallId: String,
    val startTime: String,
    val movie: MovieDto
)