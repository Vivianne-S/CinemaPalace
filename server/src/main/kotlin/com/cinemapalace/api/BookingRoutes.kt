package com.cinemapalace.api

import com.cinemapalace.data.booking.BookingRepository
import com.cinemapalace.domain.models.CreateBookingRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bookingRoutes() {
    val repo = BookingRepository()

    authenticate("auth-jwt") {
        route("/bookings") {

            // ✅ Skapa bokning
            post {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = principal.payload.getClaim("userId").asString()

                val req = call.receive<CreateBookingRequest>()
                if (req.seats.isEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "At least one seat is required"))
                    return@post
                }

                // Viktigt: vi använder repo, inte bookingRepository eller request
                val booking = repo.createBooking(userId, req.movieId, req.seats, req.showtime)

                // Logga i servern så vi ser vad som händer
                println("✅ Ny bokning skapad: $booking")

                call.respond(HttpStatusCode.Created, booking)
            }

            // ✅ Hämta mina bokningar
            get("/my") {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = principal.payload.getClaim("userId").asString()
                val bookings = repo.forUser(userId)
                call.respond(HttpStatusCode.OK, bookings)
            }

            // ✅ Avboka
            delete("/{id}") {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = principal.payload.getClaim("userId").asString()
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Booking id is required"))
                    return@delete
                }

                val ok = repo.delete(id, userId)
                if (ok) {
                    call.respond(HttpStatusCode.OK, mapOf("status" to "deleted"))
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Not found"))
                }
            }
        }
    }
}