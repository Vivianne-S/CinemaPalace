package com.cinemapalace

import com.cinemapalace.model.Movie
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) { jackson() }

        routing {
            get("/ping") { call.respondText("pong") }

            get("/movies") {
                val movies = listOf(
                    Movie("1", "Inception", "Dream within a dream", 2010),
                    Movie("2", "Interstellar", "Love & gravity", 2014),
                    Movie("3", "Dune: Part One", "Arrakis och kryddan", 2021)
                )
                call.respond(movies)
            }
        }
    }.start(wait = true)
}