package com.cinemapalace.api

import com.cinemapalace.data.screening.ScreeningRepository
import com.cinemapalace.domain.models.CreateScreeningRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime

fun Route.screeningRoutes() {
    val repo = ScreeningRepository()

    route("/screenings") {
        // Skapa en föreställning
        post {
            val req = call.receive<CreateScreeningRequest>()
            val startsAt = LocalDateTime.parse(req.startsAt) // "YYYY-MM-DDTHH:mm"
            val created = repo.create(req.movieId, req.theaterId, startsAt, req.totalSeats)
            call.respond(created)
        }

        // Lista föreställningar för en film
        get("/movie/{movieId}") {
            val movieId = call.parameters["movieId"]?.toIntOrNull()
                ?: return@get call.respond(mapOf("error" to "movieId must be Int"))
            call.respond(repo.listByMovie(movieId))
        }

        // Lista föreställningar för en biograf
        get("/theater/{theaterId}") {
            val theaterId = call.parameters["theaterId"]
                ?: return@get call.respond(mapOf("error" to "Missing theaterId"))
            call.respond(repo.listByTheater(theaterId))
        }

        // Hämta en specifik föreställning
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(mapOf("error" to "Missing id"))
            val s = repo.get(id) ?: return@get call.respond(mapOf("error" to "Not found"))
            call.respond(s)
        }
    }
}