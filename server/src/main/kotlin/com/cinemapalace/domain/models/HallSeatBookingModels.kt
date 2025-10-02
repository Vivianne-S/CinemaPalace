package com.cinemapalace.domain.models

// ---------- Entities ----------
data class Hall(
    val id: String,
    val theaterId: String,
    val name: String,
    val rows: Int,
    val cols: Int
)

data class Seat(
    val id: String,
    val hallId: String,
    val row: Int,
    val col: Int,
    val type: String,   // NORMAL, WHEELCHAIR, VIP
    val status: String  // FREE, RESERVED, BOOKED
)

// En bokning på stol-nivå
data class SeatBooking(
    val id: String,
    val showtimeId: String,
    val seatId: String,
    val userId: String,
    val status: String  // PENDING, CONFIRMED
)

// ---------- Requests ----------
data class CreateHallRequest(
    val theaterId: String,
    val name: String,
    val rows: Int,
    val cols: Int
)

data class CreateSeatBookingRequest(
    val showtimeId: String,
    val seatId: String,
    val userId: String
)