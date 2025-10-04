package com.cinemapalace.data.seat

import com.cinemapalace.database.SeatsTable
import com.cinemapalace.database.SeatBookingsTable
import com.cinemapalace.domain.models.SeatWithStatus
import com.cinemapalace.domain.models.BookingStatus
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class SeatSelectionRepository {

    /**
     * Hämtar alla platser för en given showtime och sätter aktuell status:
     *  - CONFIRMED  → BOOKED
     *  - RESERVED   → RESERVED
     *  - CANCELLED  → FREE
     *  - ingen bokning → FREE
     */
    fun getSeatsWithStatus(showtimeId: String): List<SeatWithStatus> = transaction {
        val allSeats = SeatsTable.selectAll().map {
            SeatWithStatus(
                id = it[SeatsTable.id],
                hallId = it[SeatsTable.hallId],
                row = it[SeatsTable.row],
                col = it[SeatsTable.col],
                type = it[SeatsTable.type],
                status = BookingStatus.FREE
            )
        }

        val bookings = SeatBookingsTable
            .select { SeatBookingsTable.showtimeId eq showtimeId }
            .associateBy(
                { it[SeatBookingsTable.seatId] },
                { BookingStatus.from(it[SeatBookingsTable.status]) }
            )

        allSeats.map { seat ->
            val bookingStatus = bookings[seat.id]
            seat.copy(
                status = when (bookingStatus) {
                    BookingStatus.CONFIRMED -> BookingStatus.BOOKED
                    BookingStatus.RESERVED -> BookingStatus.RESERVED
                    BookingStatus.CANCELLED -> BookingStatus.FREE
                    else -> BookingStatus.FREE
                }
            )
        }
    }

    /** Hämta status för en specifik bokning */
    fun getBookingStatus(bookingId: String): BookingStatus? = transaction {
        SeatBookingsTable
            .select { SeatBookingsTable.id eq bookingId }
            .map { BookingStatus.from(it[SeatBookingsTable.status]) }
            .firstOrNull()
    }

    /** Avbokar en plats (ändrar status → CANCELLED) */
    fun cancelBooking(bookingId: String): Boolean = transaction {
        val updated = SeatBookingsTable.update({ SeatBookingsTable.id eq bookingId }) {
            it[status] = BookingStatus.CANCELLED.value
        }
        updated > 0
    }

    /** Hämta endast lediga platser för en given showtime */
    fun getAvailableSeats(showtimeId: String): List<SeatWithStatus> {
        return getSeatsWithStatus(showtimeId)
            .filter { it.status == BookingStatus.FREE }
    }
}