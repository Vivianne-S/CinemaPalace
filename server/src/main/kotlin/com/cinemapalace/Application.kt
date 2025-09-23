package com.cinemapalace

import com.cinemapalace.auth.authRoutes
import com.cinemapalace.model.Movie
import com.cinemapalace.database.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.serialization.jackson.*

// 🔹 En enkel lista i minnet där vi sparar filmer
val movies = mutableListOf(
    Movie("1", "Inception", "Dream within a dream", 2010),
    Movie("2", "Interstellar", "Love & gravity", 2014),
    Movie("3", "Dune: Part One", "Arrakis och kryddan", 2021)
)

fun main() {
    embeddedServer(Netty, port = 8080) {
        // Initiera SQLite
        DatabaseFactory.init()

        install(ContentNegotiation) {
            jackson {
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            }
        }

        routing {
            // Health check
            get("/ping") { call.respondText("pong") }

            // Hämta alla filmer
            get("/movies") {
                call.respond(movies)
            }

            // Lägg till en ny film
            post("/movies") {
                val movie = call.receive<Movie>()
                movies.add(movie)
                call.respond(mapOf("message" to "Movie added!", "movie" to movie))
            }

            // 🔹 Auth endpoints (nu korrekt inuti routing)
            authRoutes()
        }
    }.start(wait = true)
}