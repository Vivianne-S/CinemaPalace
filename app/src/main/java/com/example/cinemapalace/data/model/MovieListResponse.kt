package com.example.cinemapalace.data.model

import com.google.gson.annotations.SerializedName

// 🔹 TMDB API Response
data class MovieListResponse(
    @SerializedName("results")
    val results: List<TmdbMovie>
)

// 🔹 TMDB Movie model
data class TmdbMovie(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("vote_average")
    val rating: Double?
)