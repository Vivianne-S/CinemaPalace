package com.cinemapalace.domain.models

data class SeatWithStatus(
    val id: String,
    val hallId: String,
    val row: Int,
    val col: Int,
    val type: String,
    val status: BookingStatus // FREE / RESERVED / BOOKED / CANCELLED
)