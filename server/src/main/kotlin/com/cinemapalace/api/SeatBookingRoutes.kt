package com.cinemapalace.api

import com.cinemapalace.data.booking.SeatBookingRepository
import com.cinemapalace.domain.models.CreateSeatBookingRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.seatBookingRoutes() {
    val repo = SeatBookingRepository()

    route("/seat-bookings") {
        post {
            val req = call.receive<CreateSeatBookingRequest>()
            val booking = repo.create(req.showtimeId, req.seatId, req.userId)
            call.respond(booking)
        }

        get("/{showtimeId}") {
            val showtimeId = call.parameters["showtimeId"] ?: return@get call.respond(mapOf("error" to "Missing showtime id"))
            val bookings = repo.listForShowtime(showtimeId)
            call.respond(bookings)
        }

        put("/{id}/confirm") {
            val id = call.parameters["id"] ?: return@put call.respond(mapOf("error" to "Missing booking id"))
            val ok = repo.confirm(id)
            if (ok) call.respond(mapOf("status" to "confirmed", "id" to id))
            else call.respond(mapOf("error" to "Booking not found"))
        }
    }
}