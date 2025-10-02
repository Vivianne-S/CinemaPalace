package com.cinemapalace.domain.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TmdbMovieListResponse(
    val page: Int,
    @JsonProperty("results") val results: List<TmdbMovieSummary>,
    @JsonProperty("total_pages") val totalPages: Int,
    @JsonProperty("total_results") val totalResults: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TmdbMovieSummary(
    val id: Int,
    val title: String,
    @JsonProperty("overview") val overview: String,
    @JsonProperty("release_date") val releaseDate: String?,
    @JsonProperty("poster_path") val posterPath: String?,
    @JsonProperty("backdrop_path") val backdropPath: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TmdbMovieDetailResponse(
    val id: Int,
    val title: String,
    val overview: String,
    @JsonProperty("release_date") val releaseDate: String?,
    val runtime: Int?,
    val genres: List<TmdbGenre>?,
    @JsonProperty("poster_path") val posterPath: String?,
    @JsonProperty("backdrop_path") val backdropPath: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TmdbGenre(
    val id: Int,
    val name: String
)