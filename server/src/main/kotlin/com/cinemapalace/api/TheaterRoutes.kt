package com.cinemapalace.api

import com.cinemapalace.data.theater.TheaterRepository
import com.cinemapalace.domain.models.CreateTheaterRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.theaterRoutes() {
    val repo = TheaterRepository()

    route("/theaters") {
        get {
            call.respond(repo.list())
        }
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(mapOf("error" to "Missing id"))
            val theater = repo.get(id) ?: return@get call.respond(mapOf("error" to "Not found"))
            call.respond(theater)
        }
        post {
            val req = call.receive<CreateTheaterRequest>()
            val theater = repo.create(req.name, req.city)
            call.respond(theater)
        }
    }
}