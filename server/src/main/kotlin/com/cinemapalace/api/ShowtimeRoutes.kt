package com.cinemapalace.api

import com.cinemapalace.data.showtime.ShowtimesRepository
import com.cinemapalace.domain.models.CreateShowtimeRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.showtimeRoutes() {
    val repo = ShowtimesRepository()

    route("/showtimes") {
        // ✅ Skapa ny föreställning
        post {
            val req = call.receive<CreateShowtimeRequest>()
            val showtime = repo.create(req.theaterId, req.movieId, req.hall, req.startTime)
            call.respond(showtime)
        }

        // ✅ Lista alla föreställningar
        get {
            val all = repo.listAll()
            call.respond(all)
        }

        // ✅ Ta bort en specifik föreställning
        delete("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(mapOf("error" to "Showtime id is required"))
                return@delete
            }

            val deleted = repo.delete(id)
            if (deleted) {
                call.respond(mapOf("status" to "deleted"))
            } else {
                call.respond(mapOf("error" to "Not found"))
            }
        }
    }

    // ✅ Hämta alla föreställningar för en specifik biograf
    route("/theaters/{id}/showtimes") {
        get {
            val theaterId = call.parameters["id"]
                ?: return@get call.respond(mapOf("error" to "Missing theater id"))

            val showtimes = repo.forTheater(theaterId)
            call.respond(showtimes)
        }
    }
}