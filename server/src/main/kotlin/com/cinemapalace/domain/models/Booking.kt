package com.cinemapalace.domain.models

data class Booking(
    val id: String,
    val userId: String,
    val movieId: Int,
    val seats: List<String>,
    val showtime: String,
    val createdAt: String
)

data class CreateBookingRequest(
    val movieId: Int,
    val seats: List<String>,
    val showtime: String
)