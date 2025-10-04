package com.cinemapalace.domain.models

// ---------- Showtime ----------
data class Showtime(
    val id: String,
    val theaterId: String,
    val movieId: Int,
    val hallId: String,        // ✅ hallId istället för hall
    val startTime: String
)

data class CreateShowtimeRequest(
    val theaterId: String,
    val movieId: Int,
    val hallId: String,
    val startTime: String
)

// ---------- Theater ----------
data class Theater(
    val id: String,
    val name: String,
    val city: String
)

data class CreateTheaterRequest(
    val name: String,
    val city: String
)

// ---------- Movie + Showtime med Movie ----------
data class ShowtimeWithMovie(
    val id: String,
    val theaterId: String,
    val hallId: String,        // ✅ hallId istället för hall
    val startTime: String,
    val movie: MovieDto
)

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?
)