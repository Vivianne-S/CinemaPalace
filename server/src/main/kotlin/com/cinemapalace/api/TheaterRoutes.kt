package com.cinemapalace.api

import com.cinemapalace.data.theater.TheaterRepository
import com.cinemapalace.domain.models.TheaterDto
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

data class CreateTheaterRequest(val name: String, val city: String)

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
        // En enkel create för seed / admin. (Lås gärna bakom auth i produktion)
        post {
            val req = call.receive<CreateTheaterRequest>()
            val theater: TheaterDto = repo.create(req.name, req.city)
            call.respond(theater)
        }
    }
}