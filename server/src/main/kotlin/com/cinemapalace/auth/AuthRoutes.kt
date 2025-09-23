package com.cinemapalace.auth

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    val userRepository = UserRepository()

    // Registrera
    post("/register") {
        val request = call.receive<RegisterRequest>()

        val existing = userRepository.findByEmail(request.email)
        if (existing != null) {
            call.respond(AuthResponse(existing.id, "User already exists"))
            return@post
        }

        val user = userRepository.createUser(request.name, request.email, request.password)
        call.respond(AuthResponse(user.id, "User ${user.name} registered with email ${user.email}"))
    }

    // Login
    post("/login") {
        val request = call.receive<LoginRequest>()
        val user = userRepository.validateUser(request.email, request.password)

        if (user == null) {
            call.respond(AuthResponse(null, "Invalid credentials"))
            return@post
        }

        call.respond(AuthResponse(user.id, "Login successful for ${user.email}"))
    }
}