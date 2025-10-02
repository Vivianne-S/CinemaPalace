package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table

object SeatBookingsTable : Table("seat_bookings") {
    val id = varchar("id", 50)
    val showtimeId = varchar("showtime_id", 50) references ShowtimesTable.id
    val seatId = varchar("seat_id", 50) references SeatsTable.id
    val userId = varchar("user_id", 50) // mock-användare tills vidare
    val status = varchar("status", 50)  // PENDING, CONFIRMED
    override val primaryKey = PrimaryKey(id)
}