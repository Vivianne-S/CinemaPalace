package com.cinemapalace.domain.models

data class ShowtimeWithMovie(
    val id: String,
    val theaterId: String,
    val hall: String,
    val startTime: String,
    val movie: MovieDto
)

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?
)