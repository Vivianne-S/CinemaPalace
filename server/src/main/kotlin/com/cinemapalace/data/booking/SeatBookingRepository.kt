package com.cinemapalace.data.booking

import com.cinemapalace.database.SeatBookingsTable
import com.cinemapalace.domain.models.SeatBooking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SeatBookingRepository {
    private fun rowToBooking(row: ResultRow) = SeatBooking(
        id = row[SeatBookingsTable.id],
        showtimeId = row[SeatBookingsTable.showtimeId],
        seatId = row[SeatBookingsTable.seatId],
        userId = row[SeatBookingsTable.userId],
        status = row[SeatBookingsTable.status]
    )

    fun create(showtimeId: String, seatId: String, userId: String, status: String = "PENDING"): SeatBooking = transaction {
        val id = UUID.randomUUID().toString()
        SeatBookingsTable.insert {
            it[SeatBookingsTable.id] = id
            it[SeatBookingsTable.showtimeId] = showtimeId
            it[SeatBookingsTable.seatId] = seatId
            it[SeatBookingsTable.userId] = userId
            it[SeatBookingsTable.status] = status
        }
        SeatBooking(id, showtimeId, seatId, userId, status)
    }

    fun listForShowtime(showtimeId: String): List<SeatBooking> = transaction {
        SeatBookingsTable.select { SeatBookingsTable.showtimeId eq showtimeId }
            .map(::rowToBooking)
    }

    fun confirm(id: String): Boolean = transaction {
        SeatBookingsTable.update({ SeatBookingsTable.id eq id }) {
            it[SeatBookingsTable.status] = "CONFIRMED"
        } > 0
    }
}