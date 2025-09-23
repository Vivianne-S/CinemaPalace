package com.cinemapalace.auth

import com.cinemapalace.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {

    post("/register") {
        val req = call.receive<RegisterRequest>()
        val user = UserRepository.register(req.name, req.email, req.password)
        call.respond(AuthResponse(user.id, "User ${user.name} registered with email ${user.email}"))
    }

    post("/login") {
        val req = call.receive<LoginRequest>()
        val user = UserRepository.login(req.email, req.password)
        if (user != null) {
            call.respond(AuthResponse(user.id, "Login successful for ${user.email}"))
        } else {
            call.respond(AuthResponse(null, "Invalid credentials"))
        }
    }
}