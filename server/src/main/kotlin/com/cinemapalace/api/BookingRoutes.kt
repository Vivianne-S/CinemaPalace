package com.cinemapalace.api

import com.cinemapalace.data.booking.BookingRepository
import com.cinemapalace.domain.models.CreateBookingRequest
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
            // Skapa bokning (kräver inlogg)
            post {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = principal.payload.getClaim("userId").asString()
                val req = call.receive<CreateBookingRequest>()

                try {
                    val booking = repo.create(userId, req.screeningId, req.seats)
                    call.respond(mapOf(
                        "message" to "Booking created",
                        "booking" to booking
                    ))
                } catch (e: IllegalStateException) {
                    call.respond(mapOf("error" to e.message))
                }
            }

            // Mina bokningar
            get("/me") {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = principal.payload.getClaim("userId").asString()
                call.respond(repo.listByUser(userId))
            }
        }
    }
}