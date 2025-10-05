package com.cinemapalace.seed

import com.cinemapalace.config.TmdbConfig
import com.cinemapalace.data.movie.TmdbRepository
import com.cinemapalace.data.showtime.ShowtimesRepository
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

object SeedData {

    fun populate(tmdbConfig: TmdbConfig, client: HttpClient) = runBlocking {
        val showtimeRepo = ShowtimesRepository()
        val tmdbRepo = TmdbRepository(client, tmdbConfig)

        // 🎬 Hämta aktuella filmer från TMDB
        val movies = tmdbRepo.getNowPlayingMovies()
        if (movies.results.isEmpty()) {
            println("⚠️ Inga filmer hittades på TMDB (kontrollera API-nyckeln)")
            return@runBlocking
        }

        println("🎬 Hittade ${movies.results.size} filmer på bio just nu")

        val baseTime = LocalDateTime.now().withHour(18).withMinute(0)
        val theaters = listOf("gbg-filmstaden", "malmo-filmstaden", "swe-bio")

        // 🚫 Undvik dubletter
        val existingShowtimes = showtimeRepo.listAll()
        val existingMovieIds = existingShowtimes.map { it.movieId }.toSet()

        for ((index, movie) in movies.results.take(10).withIndex()) {
            if (movie.id in existingMovieIds) {
                println("⏭️ Hoppar över ${movie.title} (redan i databasen)")
                continue
            }

            val theater = theaters[index % theaters.size]
            val hallId = "salong${(1..5).random()}"

            showtimeRepo.create(
                theaterId = theater,
                movieId = movie.id,
                hallId = hallId,
                startTime = baseTime.plusDays(index.toLong()).toString()
            )

            println("✅ La till ${movie.title} i $theater ($hallId)")
        }

        println("🎉 Seedning klar!")
    }
}