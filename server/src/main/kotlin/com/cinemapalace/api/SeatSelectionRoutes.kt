package com.cinemapalace.api

import com.cinemapalace.data.seat.SeatSelectionRepository
import com.cinemapalace.data.booking.SeatBookingRepository
import com.cinemapalace.database.SeatBookingsTable
import com.cinemapalace.domain.models.BookingStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

// 🔹 Request-modell för att reservera flera platser
data class ReserveSeatsRequest(
    val showtimeId: String,
    val seatIds: List<String>,
    val userId: String
)

fun Route.seatSelectionRoutes() {
    val seatRepo = SeatSelectionRepository()
    val bookingRepo = SeatBookingRepository()

    route("/seat-selection") {

        // ✅ GET – alla platser för en viss showtime
        get("/{showtimeId}") {
            val showtimeId = call.parameters["showtimeId"]
            if (showtimeId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing showtimeId"))
                return@get
            }

            val seats = seatRepo.getSeatsWithStatus(showtimeId)
            call.respond(seats)
        }

        // ✅ POST – reservera flera platser
        post("/reserve") {
            val req = call.receive<ReserveSeatsRequest>()
            val reserved = req.seatIds.map { seatId ->
                bookingRepo.create(
                    showtimeId = req.showtimeId,
                    seatId = seatId,
                    userId = req.userId,
                    status = BookingStatus.RESERVED.value
                )
            }
            call.respond(reserved)
        }

        // ✅ PUT – bekräfta bokning
        put("/{bookingId}/confirm") {
            val bookingId = call.parameters["bookingId"]
            if (bookingId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing bookingId"))
                return@put
            }

            val ok = bookingRepo.confirm(bookingId)
            if (ok) call.respond(mapOf("status" to BookingStatus.BOOKED.value))
            else call.respond(HttpStatusCode.NotFound, mapOf("error" to "Booking not found"))
        }

        // ✅ PUT – avboka plats (ändra status till CANCELLED)
        put("/{bookingId}/cancel") {
            val bookingId = call.parameters["bookingId"]
            if (bookingId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing bookingId"))
                return@put
            }

            val ok = seatRepo.cancelBooking(bookingId)
            if (ok) call.respond(mapOf("status" to BookingStatus.CANCELLED.value))
            else call.respond(HttpStatusCode.NotFound, mapOf("error" to "Booking not found"))
        }

        // ✅ GET – hämta status för en specifik bokning
        get("/status/{bookingId}") {
            val bookingId = call.parameters["bookingId"]
            if (bookingId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing bookingId"))
                return@get
            }

            val status = seatRepo.getBookingStatus(bookingId)
            if (status != null) {
                call.respond(mapOf("bookingId" to bookingId, "status" to status.value))
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Booking not found"))
            }
        }

        // ✅ GET – endast lediga platser för en viss showtime
        get("/available/{showtimeId}") {
            val showtimeId = call.parameters["showtimeId"]
            if (showtimeId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing showtimeId"))
                return@get
            }

            val availableSeats = seatRepo.getAvailableSeats(showtimeId)
            call.respond(availableSeats)
        }

        // ✅ DELETE – ta bort en specifik bokning permanent
        delete("/{bookingId}") {
            val bookingId = call.parameters["bookingId"]
            if (bookingId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing bookingId"))
                return@delete
            }

            val deletedCount = transaction {
                SeatBookingsTable.deleteWhere { SeatBookingsTable.id eq bookingId }
            }

            if (deletedCount > 0) {
                call.respond(mapOf("status" to "deleted", "bookingId" to bookingId))
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Booking not found"))
            }
        }

        // ✅ DELETE – ta bort ALLA CANCELLED-bokningar (admin cleanup)
        delete("/cleanup-cancelled") {
            val deletedCount = transaction {
                SeatBookingsTable.deleteWhere { SeatBookingsTable.status eq BookingStatus.CANCELLED.value }
            }

            call.respond(
                mapOf(
                    "message" to "Cleanup complete",
                    "deletedCount" to deletedCount
                )
            )
        }
    }

    // ✅ GET – lista alla bokningar för en viss showtime
    get("/seat-bookings") {
        val showtimeId = call.request.queryParameters["showtimeId"]
        if (showtimeId == null) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing showtimeId"))
            return@get
        }

        val bookings = transaction {
            SeatBookingsTable.select { SeatBookingsTable.showtimeId eq showtimeId }
                .map {
                    mapOf(
                        "id" to it[SeatBookingsTable.id],
                        "showtimeId" to it[SeatBookingsTable.showtimeId],
                        "seatId" to it[SeatBookingsTable.seatId],
                        "userId" to it[SeatBookingsTable.userId],
                        "status" to it[SeatBookingsTable.status]
                    )
                }
        }

        call.respond(bookings)
    }
}