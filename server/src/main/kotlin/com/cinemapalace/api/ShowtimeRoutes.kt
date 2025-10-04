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

    suspend fun mapToDto(st: com.cinemapalace.domain.models.Showtime): ShowtimeWithMovie {
        val movie = tmdbRepo.getMovieDetails(st.movieId.toString())
        return ShowtimeWithMovie(
            id = st.id,
            theaterId = st.theaterId,
            hallId = st.hallId,
            startTime = st.startTime,
            movie = MovieDto(
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                posterPath = movie.posterPath
            )
        )
    }

    route("/showtimes") {
        post {
        val req = call.receive<CreateShowtimeRequest>()
        call.respond(repo.create(req.theaterId, req.movieId, req.hallId, req.startTime))
    }

        get { call.respond(repo.listAll().map { mapToDto(it) }) }

        get("/theater/{theaterId}") {
            val theaterId = call.paramOrError("theaterId") ?: return@get
            call.respond(repo.getByTheater(theaterId).map { mapToDto(it) })
        }

        get("/{id}") {
            val id = call.paramOrError("id") ?: return@get
            repo.get(id)?.let { call.respond(it) }
                ?: call.respond(mapOf("error" to "Not found"))
        }

        put("/{id}") {
            val id = call.paramOrError("id") ?: return@put
            val req = call.receive<CreateShowtimeRequest>()
            repo.update(id, req.theaterId, req.movieId, req.hallId, req.startTime)
                ?.let { call.respond(it) }
                ?: call.respond(mapOf("status" to "not found", "id" to id))
        }

        delete("/{id}") {
            val id = call.paramOrError("id") ?: return@delete
            if (repo.delete(id)) call.respond(mapOf("status" to "deleted", "id" to id))
            else call.respond(mapOf("status" to "not found", "id" to id))
        }
    }
}