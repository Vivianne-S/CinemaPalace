package com.cinemapalace.api

import com.cinemapalace.data.showtime.ShowtimesRepository
import com.cinemapalace.domain.models.CreateShowtimeRequest
import com.cinemapalace.domain.models.ShowtimeWithMovie
import com.cinemapalace.domain.models.MovieDto
import com.cinemapalace.config.TmdbConfig
import com.cinemapalace.data.movie.TmdbRepository
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.showtimeRoutes(tmdbConfig: TmdbConfig, client: HttpClient) {
    val repo = ShowtimesRepository()
    val tmdbRepo = TmdbRepository(client, tmdbConfig)

    route("/showtimes") {

        // ➕ Skapa en ny föreställning
        post {
            val req = call.receive<CreateShowtimeRequest>()
            val showtime = repo.create(req.theaterId, req.movieId, req.hall, req.startTime)
            call.respond(showtime)
        }

        // 📋 Lista alla föreställningar (inkl. filmdata)
        get {
            val all = repo.listAll()
            val result = all.map { st ->
                val movie = tmdbRepo.getMovieDetails(st.movieId.toString())
                ShowtimeWithMovie(
                    id = st.id,
                    theaterId = st.theaterId,
                    hall = st.hall,
                    startTime = st.startTime,
                    movie = MovieDto(
                        id = movie.id,
                        title = movie.title,
                        overview = movie.overview,
                        posterPath = movie.posterPath
                    )
                )
            }
            call.respond(result)
        }

        // 🎭 Lista alla föreställningar för en specifik biograf
        get("/theater/{theaterId}") {
            val theaterId = call.parameters["theaterId"]
                ?: return@get call.respond(mapOf("error" to "Missing theaterId"))

            val showtimes = repo.getByTheater(theaterId)
            val result = showtimes.map { st ->
                val movie = tmdbRepo.getMovieDetails(st.movieId.toString())
                ShowtimeWithMovie(
                    id = st.id,
                    theaterId = st.theaterId,
                    hall = st.hall,
                    startTime = st.startTime,
                    movie = MovieDto(
                        id = movie.id,
                        title = movie.title,
                        overview = movie.overview,
                        posterPath = movie.posterPath
                    )
                )
            }
            call.respond(result)
        }

        // 🔍 Hämta en specifik föreställning
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(mapOf("error" to "Missing id"))
            val showtime = repo.get(id) ?: return@get call.respond(mapOf("error" to "Not found"))
            call.respond(showtime)
        }

        // ✏️ Uppdatera en föreställning
        put("/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(mapOf("error" to "Missing showtime id"))
            } else {
                val req = call.receive<CreateShowtimeRequest>()
                val updated = repo.update(id, req.theaterId, req.movieId, req.hall, req.startTime)
                if (updated != null) {
                    call.respond(updated)
                } else {
                    call.respond(mapOf("status" to "not found", "id" to id))
                }
            }
        }

        // ❌ Ta bort en föreställning
        delete("/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(mapOf("error" to "Missing showtime id"))
            } else {
                val deleted = repo.delete(id)
                if (deleted) {
                    call.respond(mapOf("status" to "deleted", "id" to id))
                } else {
                    call.respond(mapOf("status" to "not found", "id" to id))
                }
            }
        }
    }
}