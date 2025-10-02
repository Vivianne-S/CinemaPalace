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
        get("/popular") {
            try {
                val response = repository.getPopularMovies()
                call.respond(response)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(mapOf("error" to "Failed to fetch popular movies: ${e.message}"))
            }
        }

        get("/search") {
            val query = call.request.queryParameters["query"]
                ?: return@get call.respond(mapOf("error" to "Query parameter is required"))
            call.respond(repository.searchMovies(query))
        }

        get("/movie/{id}") {
            val id = call.parameters["id"]
                ?: return@get call.respond(mapOf("error" to "Movie ID is required"))
            call.respond(repository.getMovieDetails(id))
        }
    }
}