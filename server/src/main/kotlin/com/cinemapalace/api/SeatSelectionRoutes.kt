package com.cinemapalace.api

import com.cinemapalace.data.seat.SeatRepository
import com.cinemapalace.data.booking.SeatBookingRepository
import com.cinemapalace.domain.models.BookingStatus
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Request-modell för reservering
data class ReserveSeatsRequest(
    val showtimeId: String,
    val seatIds: List<String>,
    val userId: String
)

fun Route.seatSelectionRoutes() {
    val seatRepo = SeatRepository()
    val bookingRepo = SeatBookingRepository()

    route("/seat-selection") {

        // ✅ GET: alla seats för en showtime
        get("/{showtimeId}") {
            val showtimeId = call.paramOrError("showtimeId") ?: return@get
            val seats = seatRepo.listForShowtime(showtimeId)
            call.respond(seats)
        }

        // ✅ POST: reservera flera seats
        post("/reserve") {
            val req = call.receive<ReserveSeatsRequest>()
            val reserved = req.seatIds.map { seatId ->
                bookingRepo.create(
                    showtimeId = req.showtimeId,
                    seatId = seatId,
                    userId = req.userId,
                    status = BookingStatus.RESERVED
                )
            }
            call.respond(reserved)
        }
// ✅ PUT: bekräfta
        put("/{bookingId}/confirm") {
            val bookingId = call.paramOrError("bookingId") ?: return@put
            if (bookingRepo.confirm(bookingId)) {
                call.respond(mapOf("status" to BookingStatus.BOOKED.value, "id" to bookingId))
            } else {
                call.respond(mapOf("error" to "Booking not found"))
            }
        }

// ✅ PUT: avboka
        put("/{bookingId}/cancel") {
            val bookingId = call.paramOrError("bookingId") ?: return@put
            if (bookingRepo.cancel(bookingId)) {
                call.respond(mapOf("status" to BookingStatus.CANCELLED.value, "id" to bookingId))
            } else {
                call.respond(mapOf("error" to "Booking not found"))
            }
        }
    }
}