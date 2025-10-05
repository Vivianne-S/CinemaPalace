package com.cinemapalace

import com.cinemapalace.api.*
import com.cinemapalace.config.AppConfig
import com.cinemapalace.database.DatabaseFactory
import com.cinemapalace.seed.SeedData
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.cinemapalace.data.showtime.ShowtimesRepository

fun main() {
    val dotenv = dotenv {
        ignoreIfMissing = true
    }
    dotenv.entries().forEach { entry ->
        System.setProperty(entry.key, entry.value)
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    val appConfig = AppConfig.fromApplicationConfig(environment.config)

    // ✅ Init DB
    DatabaseFactory.init(appConfig.database)

    // ✅ HttpClient för TMDB
    val client = HttpClient {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            jackson {
                configure(SerializationFeature.INDENT_OUTPUT, true)
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
    }

    // ✅ CORS support för Android app
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
    }

    // ✅ Serverns ContentNegotiation
    install(ContentNegotiation) {
        jackson {
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }

    // ✅ JWT Authentication
    install(Authentication) {
        jwt("auth-jwt") {
            realm = appConfig.jwt.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(appConfig.jwt.secret))
                    .withAudience(appConfig.jwt.audience)
                    .withIssuer(appConfig.jwt.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asString().isNotEmpty()) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }

    // ✅ SeedData körs bara första gången (när DB är tom)
    val showtimeRepo = ShowtimesRepository()
    val existing = showtimeRepo.listAll()

    if (existing.isEmpty()) {
        println("🌱 Ingen data hittades i databasen – kör SeedData...")
        SeedData.populate(appConfig.tmdb, client)
    } else {
        println("✅ Databasen innehåller redan ${existing.size} showtimes – seed hoppas över.")
    }

    // ✅ Routing
    routing {
        get("/health") {
            call.respond(mapOf("status" to "healthy"))
        }

        // Biograf-routes
        theaterRoutes()

        // TMDB-routes
        movieRoutes(appConfig.tmdb, client)

        // Auth-routes
        route("/auth") {
            authRoutes(appConfig.jwt)
            get("/ping") { call.respond(mapOf("status" to "auth alive")) }
        }

        // Showtimes-routes - ✅ Denna hanterar /showtimes
        showtimeRoutes(appConfig.tmdb, client)

        // Filmstaden-style routes
        hallRoutes()
        seatBookingRoutes()
        seatSelectionRoutes()
    }
}