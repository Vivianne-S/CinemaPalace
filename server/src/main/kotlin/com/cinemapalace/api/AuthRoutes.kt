package com.cinemapalace.api

import com.cinemapalace.config.JwtConfig
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.cinemapalace.data.auth.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.authRoutes(jwtConfig: JwtConfig) {
    val userRepository = UserRepository()

    // ✅ Register
    post("/register") {
        val request = call.receive<RegisterRequest>()

        val existing = userRepository.findByEmail(request.email)
        if (existing != null) {
            call.respond(AuthResponse(null, "User already exists"))
            return@post
        }

        val user = userRepository.createUser(request.name, request.email, request.password)
        call.respond(AuthResponse(user.id, "User ${user.name} registered"))
    }

    // ✅ Login with JWT
    post("/login") {
        val request = call.receive<LoginRequest>()
        val user = userRepository.validateUser(request.email, request.password)

        if (user == null) {
            call.respond(AuthResponse(null, "Invalid credentials"))
            return@post
        }

        val token = JWT.create()
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withClaim("userId", user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000)) // 1 timme
            .sign(Algorithm.HMAC256(jwtConfig.secret))

        call.respond(AuthResponse(token, "Login successful"))
    }

    // ✅ Authenticated route
    authenticate("auth-jwt") {
        get("/me") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asString()

            if (userId == null) {
                call.respond(AuthResponse(null, "No valid token"))
                return@get
            }

            val user = userRepository.findById(userId)
            if (user == null) {
                call.respond(AuthResponse(null, "User not found"))
            } else {
                call.respond(
                    mapOf(
                        "id" to user.id,
                        "name" to user.name,
                        "email" to user.email
                    )
                )
            }
        }
    }
}

// DTOs
data class RegisterRequest(val name: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class AuthResponse(val token: String?, val message: String)