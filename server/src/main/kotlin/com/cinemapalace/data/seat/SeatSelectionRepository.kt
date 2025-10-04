package com.cinemapalace.data.seat

import com.cinemapalace.database.SeatsTable
import com.cinemapalace.database.SeatBookingsTable
import com.cinemapalace.domain.models.SeatWithStatus
import com.cinemapalace.domain.models.BookingStatus
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class SeatSelectionRepository {

    /** ✅ Hämtar alla platser för en given showtime och sätter aktuell status */
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

    /** ✅ Hämta full info om en specifik bokning */
    fun getBookingDetails(bookingId: String): Map<String, Any?>? = transaction {
        SeatBookingsTable
            .select { SeatBookingsTable.id eq bookingId }
            .map {
                mapOf(
                    "bookingId" to it[SeatBookingsTable.id],
                    "showtimeId" to it[SeatBookingsTable.showtimeId],
                    "seatId" to it[SeatBookingsTable.seatId],
                    "userId" to it[SeatBookingsTable.userId],
                    "status" to it[SeatBookingsTable.status]
                )
            }
            .firstOrNull()
    }

    /** ✅ Avbokar en plats (ändrar status → CANCELLED) */
    fun cancelBooking(bookingId: String): Boolean = transaction {
        val updated = SeatBookingsTable.update({ SeatBookingsTable.id eq bookingId }) {
            it[status] = BookingStatus.CANCELLED.value
        }
        updated > 0
    }

    /** ✅ Hämta endast lediga platser för en given showtime */
    fun getAvailableSeats(showtimeId: String): List<SeatWithStatus> {
        return getSeatsWithStatus(showtimeId)
            .filter { it.status == BookingStatus.FREE }
    }

    /** ✅ Hämta alla bokningar för en specifik användare */
    fun getBookingsByUser(userId: String): List<Map<String, Any>> = transaction {
        val results = SeatBookingsTable
            .select { SeatBookingsTable.userId.lowerCase() eq userId.lowercase() }
            .map {
                mapOf(
                    "bookingId" to it[SeatBookingsTable.id],
                    "showtimeId" to it[SeatBookingsTable.showtimeId],
                    "seatId" to it[SeatBookingsTable.seatId],
                    "userId" to it[SeatBookingsTable.userId],
                    "status" to it[SeatBookingsTable.status]
                )
            }

        println("🧩 DEBUG: Hittade ${results.size} bokningar för userId=$userId")
        results
    }
}