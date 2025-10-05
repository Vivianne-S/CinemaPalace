package com.cinemapalace.data.movie

import com.cinemapalace.config.TmdbConfig
import com.cinemapalace.domain.models.TmdbMovieDetailResponse
import com.cinemapalace.domain.models.TmdbMovieListResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class TmdbRepository(
    private val client: HttpClient,
    private val config: TmdbConfig
) {
    private val mapper = jacksonObjectMapper()
    private val imageBaseUrl = "https://image.tmdb.org/t/p/w500"

    // 🔹 Topplista
    suspend fun getPopularMovies(): TmdbMovieListResponse {
        val response: HttpResponse = client.get("${config.baseUrl}/movie/popular") {
            parameter("api_key", config.apiKey)
            parameter("language", "sv-SE")
            parameter("region", "SE")
        }
        val parsed = parseResponse(response, TmdbMovieListResponse::class.java)
        return parsed.copy(
            results = parsed.results.map {
                it.copy(
                    posterPath = it.posterPath?.let { "$imageBaseUrl$it" },
                    backdropPath = it.backdropPath?.let { "$imageBaseUrl$it" }
                )
            }
        )
    }

    // 🔹 Sök
    suspend fun searchMovies(query: String): TmdbMovieListResponse {
        val response: HttpResponse = client.get("${config.baseUrl}/search/movie") {
            parameter("api_key", config.apiKey)
            parameter("language", "sv-SE")
            parameter("query", query)
        }
        val parsed = parseResponse(response, TmdbMovieListResponse::class.java)
        return parsed.copy(
            results = parsed.results.map {
                it.copy(
                    posterPath = it.posterPath?.let { "$imageBaseUrl$it" },
                    backdropPath = it.backdropPath?.let { "$imageBaseUrl$it" }
                )
            }
        )
    }

    // 🔹 Film-detaljer
    suspend fun getMovieDetails(id: String): TmdbMovieDetailResponse {
        val response: HttpResponse = client.get("${config.baseUrl}/movie/$id") {
            parameter("api_key", config.apiKey)
            parameter("language", "sv-SE")
        }
        val parsed = parseResponse(response, TmdbMovieDetailResponse::class.java)
        return parsed.copy(
            posterPath = parsed.posterPath?.let { "$imageBaseUrl$it" },
            backdropPath = parsed.backdropPath?.let { "$imageBaseUrl$it" }
        )
    }

    // 🔹 På bio nu
    suspend fun getNowPlayingMovies(): TmdbMovieListResponse {
        val response: HttpResponse = client.get("${config.baseUrl}/movie/now_playing") {
            parameter("api_key", config.apiKey)
            parameter("language", "sv-SE")
            parameter("region", "SE")
        }
        val parsed = parseResponse(response, TmdbMovieListResponse::class.java)
        return parsed.copy(
            results = parsed.results.map {
                it.copy(
                    posterPath = it.posterPath?.let { "$imageBaseUrl$it" },
                    backdropPath = it.backdropPath?.let { "$imageBaseUrl$it" }
                )
            }
        )
    }

    // 🔹 Kommande filmer
    suspend fun getUpcomingMovies(): TmdbMovieListResponse {
        val response: HttpResponse = client.get("${config.baseUrl}/movie/upcoming") {
            parameter("api_key", config.apiKey)
            parameter("language", "sv-SE")
            parameter("region", "SE")
        }
        val parsed = parseResponse(response, TmdbMovieListResponse::class.java)
        return parsed.copy(
            results = parsed.results.map {
                it.copy(
                    posterPath = it.posterPath?.let { "$imageBaseUrl$it" },
                    backdropPath = it.backdropPath?.let { "$imageBaseUrl$it" }
                )
            }
        )
    }

    // 🔹 Hjälpfunktion
    private suspend fun <T> parseResponse(response: HttpResponse, clazz: Class<T>): T {
        val text = response.bodyAsText()
        return mapper.readValue(text, clazz)
    }
}