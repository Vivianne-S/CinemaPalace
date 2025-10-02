package com.cinemapalace.api

import com.cinemapalace.data.showtime.ShowtimeRepository
import com.cinemapalace.domain.models.CreateShowtimeRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.showtimeRoutes() {
    val repo = ShowtimeRepository()

    route("/showtimes") {
        post {
            val req = call.receive<CreateShowtimeRequest>()
            val showtime = repo.create(req.theaterId, req.movieId, req.hall, req.startTime)
            call.respond(showtime)
        }
    }

    // 🎬 Hämta alla föreställningar för en specifik biograf
    route("/theaters/{id}/showtimes") {
        get {
            val theaterId = call.parameters["id"]
                ?: return@get call.respond(mapOf("error" to "Missing theater id"))

            val showtimes = repo.forTheater(theaterId)
            call.respond(showtimes)
        }
    }
}