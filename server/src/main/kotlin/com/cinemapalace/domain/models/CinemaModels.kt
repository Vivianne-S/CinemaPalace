package com.cinemapalace.domain.models

import java.time.LocalDateTime

data class TheaterDto(
    val id: String,
    val name: String,
    val city: String
)

data class ScreeningDto(
    val id: String,
    val movieId: Int,
    val theaterId: String,
    val startsAt: LocalDateTime,
    val totalSeats: Int,
    val bookedSeats: List<String>,
    val availableSeats: Int
)

data class CreateScreeningRequest(
    val movieId: Int,
    val theaterId: String,
    val startsAt: String,     // ISO, t.ex. "2025-10-02T19:30"
    val totalSeats: Int = 60
)

data class BookingDto(
    val id: String,
    val screeningId: String,
    val userId: String,
    val seats: List<String>,
    val createdAt: LocalDateTime
)