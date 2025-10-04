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
        get { call.respond(repo.list()) }

        get("/{id}") {
            val id = call.paramOrError("id") ?: return@get
            repo.get(id)?.let { call.respond(it) }
                ?: call.respond(mapOf("error" to "Not found"))
        }

        post {
            val req = call.receive<CreateTheaterRequest>()
            call.respond(repo.create(req.name, req.city))
        }

        put("/{id}") {
            val id = call.paramOrError("id") ?: return@put
            val req = call.receive<CreateTheaterRequest>()
            repo.update(id, req.name, req.city)
                ?.let { call.respond(it) }
                ?: call.respond(mapOf("error" to "Not found"))
        }

        delete("/{id}") {
            val id = call.paramOrError("id") ?: return@delete
            if (repo.delete(id)) call.respond(mapOf("status" to "deleted", "id" to id))
            else call.respond(mapOf("status" to "not found", "id" to id))
        }
    }
}