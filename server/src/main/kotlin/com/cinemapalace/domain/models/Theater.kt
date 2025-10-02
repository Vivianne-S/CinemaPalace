package com.cinemapalace.domain.models

data class Theater(
    val id: String,
    val name: String,
    val city: String
)

data class CreateTheaterRequest(
    val name: String,
    val city: String
)