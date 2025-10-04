package com.cinemapalace.data.seat

import com.cinemapalace.database.SeatsTable
import com.cinemapalace.database.SeatBookingsTable
import com.cinemapalace.domain.models.SeatWithStatus
import com.cinemapalace.domain.models.BookingStatus
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class SeatSelectionRepository {

    // Hämta alla seats för en showtime + aktuell status
    fun getSeatsWithStatus(showtimeId: String): List<SeatWithStatus> = transaction {
        val allSeats = SeatsTable.selectAll().map {
            SeatWithStatus(
                id = it[SeatsTable.id],
                hallId = it[SeatsTable.hallId],
                row = it[SeatsTable.row],
                col = it[SeatsTable.col],
                type = it[SeatsTable.type],
                status = BookingStatus.FREE // default
            )
        }

        val bookings = SeatBookingsTable
            .select { SeatBookingsTable.showtimeId eq showtimeId }
            .associateBy(
                { it[SeatBookingsTable.seatId] },
                { BookingStatus.from(it[SeatBookingsTable.status]) } // ✅ konvertera till enum
            )

        allSeats.map { seat ->
            seat.copy(status = bookings[seat.id] ?: BookingStatus.FREE)
        }
    }

    // Avboka en plats (ändra status -> CANCELLED)
    fun cancelBooking(bookingId: String): Boolean = transaction {
        val updated = SeatBookingsTable.update({ SeatBookingsTable.id eq bookingId }) {
            it[status] = BookingStatus.CANCELLED.value // ✅ spara som String i DB
        }
        updated > 0
    }
}