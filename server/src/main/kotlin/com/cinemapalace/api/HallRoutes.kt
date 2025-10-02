package com.cinemapalace.api

import com.cinemapalace.data.hall.HallRepository
import com.cinemapalace.data.seat.SeatRepository
import com.cinemapalace.domain.models.CreateHallRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.hallRoutes() {
    val repo = HallRepository()

    route("/halls") {
        post {
            val req = call.receive<CreateHallRequest>()
            val hall = repo.create(req.theaterId, req.name, req.rows, req.cols)
            call.respond(hall)
        }

        get("/{id}/seats") {
            val hallId = call.parameters["id"] ?: return@get call.respond(mapOf("error" to "Missing hall id"))
            val seatRepo = SeatRepository()
            val seats = seatRepo.listForHall(hallId)
            call.respond(seats)
        }
    }

    route("/theaters/{id}/halls") {
        get {
            val theaterId = call.parameters["id"] ?: return@get call.respond(mapOf("error" to "Missing theater id"))
            call.respond(repo.listForTheater(theaterId))
        }
    }
}