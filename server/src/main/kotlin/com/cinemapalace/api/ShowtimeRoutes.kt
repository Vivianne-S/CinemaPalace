package com.cinemapalace.api

import com.cinemapalace.config.TmdbConfig
import com.cinemapalace.data.movie.TmdbRepository
import com.cinemapalace.data.showtime.ShowtimesRepository
import com.cinemapalace.database.HallsTable
import com.cinemapalace.database.ShowtimesTable
import com.cinemapalace.database.TheatersTable
import com.cinemapalace.domain.models.*
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.showtimeRoutes(tmdbConfig: TmdbConfig, client: HttpClient) {
    val repo = ShowtimesRepository()
    val tmdb = TmdbRepository(client, tmdbConfig)

    route("/showtimes") {

        // --- GET: lista alla visningar ---
        get {
            val all = repo.listAll()
            call.respond(all)
        }

        // --- GET: hämta en specifik visning ---
        get("{id}") {
            val id = call.parameters["id"]
            val showtime = id?.let { repo.get(it) }
            if (showtime != null) call.respond(showtime)
            else call.respond(HttpStatusCode.NotFound, "Visningen hittades inte")
        }

        // --- GET: alla visningar för ett film-ID ---
        get("movie/{movieId}") {
            val movieIdParam = call.parameters["movieId"]
            if (movieIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Ogiltigt film-ID")
                return@get
            }

            try {
                val movieId = movieIdParam.toInt()
                val showtimes = repo.listAll().filter { it.movieId == movieId }
                val movieDetails = tmdb.getMovieDetails(movieIdParam)

                val result = showtimes.map {
                    ShowtimeWithMovie(
                        id = it.id,
                        theaterId = it.theaterId,
                        hallId = it.hallId,
                        startTime = it.startTime,
                        movie = MovieDto(
                            id = movieDetails.id,
                            title = movieDetails.title,
                            overview = movieDetails.overview,
                            posterPath = movieDetails.posterPath
                        )
                    )
                }

                call.respond(result)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Kunde inte hämta visningar: ${e.message}")
            }
        }

        // --- POST: skapa ny visning ---
        post {
            try {
                val body = call.receive<CreateShowtimeRequest>()
                val created = repo.create(
                    theaterId = body.theaterId,
                    movieId = body.movieId,
                    hallId = body.hallId,
                    startTime = body.startTime
                )
                call.respond(HttpStatusCode.Created, created)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, "Fel vid skapande av visning: ${e.message}")
            }
        }

        // --- PUT: uppdatera befintlig visning ---
        put("{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Saknar ID")
                return@put
            }

            val body = call.receive<Showtime>()
            val updated = repo.update(
                id = id,
                theaterId = body.theaterId,
                movieId = body.movieId,
                hallId = body.hallId,
                startTime = body.startTime
            )

            if (updated != null) call.respond(updated)
            else call.respond(HttpStatusCode.NotFound, "Visningen finns inte")
        }

        // --- DELETE: ta bort visning ---
        delete("{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Saknar ID")
                return@delete
            }

            val deleted = repo.delete(id)
            if (deleted) call.respond(HttpStatusCode.OK, "Borttagen")
            else call.respond(HttpStatusCode.NotFound, "Visningen hittades inte")
        }

        // --- Gruppvisningar per biograf ---
        get("grouped") {
            val showtimes = transaction {
                (ShowtimesTable innerJoin TheatersTable innerJoin HallsTable)
                    .selectAll()
                    .orderBy(ShowtimesTable.startTime, SortOrder.ASC)
                    .map {
                        mapOf(
                            "theaterName" to it[TheatersTable.name],
                            "hallName" to it[HallsTable.name],
                            "totalSeats" to (it[HallsTable.rows] * it[HallsTable.cols]),
                            "availableSeats" to 250,
                            "time" to it[ShowtimesTable.startTime]
                        )
                    }
            }
            call.respond(showtimes)
        }
    }
}