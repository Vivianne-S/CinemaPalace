package com.cinemapalace.api

import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.paramOrError(key: String, errorMsg: String = "Missing $key"): String? {
    val value = parameters[key]
    if (value == null) {
        respond(mapOf("error" to errorMsg))
    }
    return value
}