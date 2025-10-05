package com.cinemapalace.api

import com.cinemapalace.config.TmdbConfig
import com.cinemapalace.data.movie.TmdbRepository
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.movieRoutes(tmdbConfig: TmdbConfig, client: HttpClient) {
    val repository = TmdbRepository(client, tmdbConfig)

    route("/tmdb") {

        // 🔹 Topplista
        get("/popular") {
            try {
                val response = repository.getPopularMovies()
                call.respond(response)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(mapOf("error" to "Failed to fetch popular movies: ${e.message}"))
            }
        }

        // 🔹 På bio nu
        get("/now_playing") {
            try {
                val response = repository.getNowPlayingMovies()
                call.respond(response)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(mapOf("error" to "Failed to fetch now playing movies: ${e.message}"))
            }
        }

        // 🔹 Kommande filmer
        get("/upcoming") {
            try {
                val response = repository.getUpcomingMovies()
                call.respond(response)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(mapOf("error" to "Failed to fetch upcoming movies: ${e.message}"))
            }
        }

        // 🔹 Sök
        get("/search") {
            val query = call.request.queryParameters["query"]
                ?: return@get call.respond(mapOf("error" to "Query parameter is required"))
            try {
                val response = repository.searchMovies(query)
                call.respond(response)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(mapOf("error" to "Failed to search movies: ${e.message}"))
            }
        }

        // 🔹 Film-detaljer
        get("/movie/{id}") {
            val id = call.parameters["id"]
                ?: return@get call.respond(mapOf("error" to "Movie ID is required"))
            try {
                val response = repository.getMovieDetails(id)
                call.respond(response)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(mapOf("error" to "Failed to fetch movie details: ${e.message}"))
            }
        }
    }
}