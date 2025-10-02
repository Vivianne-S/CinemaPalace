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

        get {
            call.respond(repo.list())
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(mapOf("error" to "Missing id"))
            val showtime = repo.get(id) ?: return@get call.respond(mapOf("error" to "Not found"))
            call.respond(showtime)
        }

        get("/theater/{theaterId}") {
            val theaterId = call.parameters["theaterId"] ?: return@get call.respond(mapOf("error" to "Missing theaterId"))
            call.respond(repo.listByTheater(theaterId))
        }
    }
}